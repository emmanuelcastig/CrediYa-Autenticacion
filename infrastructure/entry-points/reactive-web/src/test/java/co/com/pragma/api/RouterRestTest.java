package co.com.pragma.api;

import co.com.pragma.api.config.NoSecurityConfig;
import co.com.pragma.api.dto.LoginRequest;
import co.com.pragma.api.dto.LoginResponse;
import co.com.pragma.api.dto.SolicitanteRequest;
import co.com.pragma.api.dto.SolicitanteResponse;
import co.com.pragma.api.mapper.SolicitanteMapper;
import co.com.pragma.api.security.JwtProvider;
import co.com.pragma.model.rol.Rol;
import co.com.pragma.model.solicitante.Solicitante;
import co.com.pragma.usecase.in.BuscarRolPort;
import co.com.pragma.usecase.in.CrearSolicitantePort;
import co.com.pragma.usecase.in.LoginSolicitantePort;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest(controllers = RouterRest.class)
@ContextConfiguration(classes = {RouterRest.class, Handler.class, RouterRestTest.TestConfig.class, NoSecurityConfig.class})
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CrearSolicitantePort crearSolicitantePort;

    @Autowired
    private LoginSolicitantePort loginSolicitantePort;

    @Autowired
    private BuscarRolPort buscarRolPort;

    @Autowired
    private Validator validator;

    @Autowired
    private SolicitanteMapper solicitanteMapper;

    @Autowired
    private TransactionalOperator transactionalOperator;

    @Autowired
    private JwtProvider jwtProvider;

    @Test
    void testCrearSolicitante() {
        var request = new SolicitanteRequest();
        var domain = new Solicitante();
        var response = new SolicitanteResponse();

        when(validator.validate(request)).thenReturn(Collections.emptySet());
        when(solicitanteMapper.toDomain(any(SolicitanteRequest.class))).thenReturn(domain);
        when(crearSolicitantePort.crearSolicitante(any(Solicitante.class))).thenReturn(Mono.just(domain));
        when(solicitanteMapper.toResponse(any(Solicitante.class))).thenReturn(response);
        when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(SolicitanteResponse.class)
                .isEqualTo(response);
    }

    @Test
    void testSolicitanteExistenteTrue() {
        when(crearSolicitantePort.solicitanteExiste("123")).thenReturn(Mono.just(true));

        webTestClient.get()
                .uri("/api/v1/usuarios/123")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.existe").isEqualTo(true)
                .jsonPath("$.mensaje").isEqualTo("El solicitante existe");
    }

    @Test
    void testSolicitanteExistenteFalse() {
        when(crearSolicitantePort.solicitanteExiste("999")).thenReturn(Mono.just(false));

        webTestClient.get()
                .uri("/api/v1/usuarios/999")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.existe").isEqualTo(false)
                .jsonPath("$.mensaje").isEqualTo("No se encontro solicitante");
    }

    @Test
    void testSolicitanteExistenteError() {
        when(crearSolicitantePort.solicitanteExiste("500"))
                .thenReturn(Mono.error(new RuntimeException("DB error")));

        webTestClient.get()
                .uri("/api/v1/usuarios/500")
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void testLoginExitoso() {
        var loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setClave("1234");

        var solicitante = new Solicitante();
        solicitante.setCorreoElectronico("test@test.com");
        solicitante.setIdRol(1L);

        var rol = new Rol();
        rol.setNombre("CLIENTE");

        when(loginSolicitantePort.login("test@test.com", "1234")).thenReturn(Mono.just(solicitante));
        when(buscarRolPort.buscarPorId(1L)).thenReturn(Mono.just(rol));
        when(jwtProvider.generateToken(any(), any())).thenReturn("fake-jwt-token");

        webTestClient.post()
                .uri("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoginResponse.class)
                .value(response -> {
                    assert response.getToken().equals("fake-jwt-token");
                    assert response.getRol().equals("CLIENTE");
                });
    }

    @Test
    void testLoginFallido() {
        var loginRequest = new LoginRequest();
        loginRequest.setEmail("wrong@test.com");
        loginRequest.setClave("wrong");

        when(loginSolicitantePort.login("wrong@test.com", "wrong"))
                .thenReturn(Mono.error(new RuntimeException("Credenciales invalidas")));

        webTestClient.post()
                .uri("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginRequest)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Credenciales invalidas");
    }

    static class TestConfig {
        @Bean CrearSolicitantePort crearSolicitantePort() { return mock(CrearSolicitantePort.class); }
        @Bean LoginSolicitantePort loginSolicitantePort() { return mock(LoginSolicitantePort.class); }
        @Bean BuscarRolPort buscarRolPort() { return mock(BuscarRolPort.class); }
        @Bean Validator validator() { return mock(Validator.class); }
        @Bean SolicitanteMapper solicitanteMapper() { return mock(SolicitanteMapper.class); }
        @Bean TransactionalOperator transactionalOperator() { return mock(TransactionalOperator.class); }
        @Bean JwtProvider jwtProvider() { return mock(JwtProvider.class); }
    }
}
