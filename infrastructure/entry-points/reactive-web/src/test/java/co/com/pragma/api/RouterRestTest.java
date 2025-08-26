package co.com.pragma.api;

import co.com.pragma.api.dto.SolicitanteRequest;
import co.com.pragma.api.dto.SolicitanteResponse;
import co.com.pragma.api.mapper.SolicitanteMapper;
import co.com.pragma.model.solicitante.Solicitante;
import co.com.pragma.usecase.in.CrearSolicitantePort;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
@Import(RouterRestTest.TestConfig.class)
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CrearSolicitantePort crearSolicitantePort;

    @Autowired
    private Validator validator;

    @Autowired
    private SolicitanteMapper solicitanteMapper;

    @Autowired
    private TransactionalOperator transactionalOperator;

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
    void testSolicitanteExistenteError() {
        when(crearSolicitantePort.solicitanteExiste("500"))
                .thenReturn(Mono.error(new RuntimeException("DB error")));

        webTestClient.get()
                .uri("/api/v1/usuarios/500")
                .exchange()
                .expectStatus().is5xxServerError();
    }

    static class TestConfig {
        @Bean CrearSolicitantePort crearSolicitantePort() { return mock(CrearSolicitantePort.class); }
        @Bean Validator validator() { return mock(Validator.class); }
        @Bean SolicitanteMapper solicitanteMapper() { return mock(SolicitanteMapper.class); }
        @Bean TransactionalOperator transactionalOperator() { return mock(TransactionalOperator.class); }
    }
}
