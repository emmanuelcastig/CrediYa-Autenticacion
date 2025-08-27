package co.com.pragma.usecase.autenticacion;

import co.com.pragma.model.solicitante.Solicitante;
import co.com.pragma.model.solicitante.gateways.SolicitanteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class AutenticacionUseCaseTest {

    private SolicitanteRepository solicitanteRepository;
    private AutenticacionUseCase autenticacionUseCase;

    @BeforeEach
    void setUp() {
        solicitanteRepository = mock(SolicitanteRepository.class);
        autenticacionUseCase = new AutenticacionUseCase(solicitanteRepository);
    }

    @Test
    void testLogin_Exitoso() {
        // Arrange
        String email = "test@correo.com";
        String clave = "12345";

        Solicitante solicitante = new Solicitante();
        solicitante.setCorreoElectronico(email);
        solicitante.setClave(clave);

        when(solicitanteRepository.findByCorreoElectronico(email))
                .thenReturn(Mono.just(solicitante));

        // Act & Assert
        StepVerifier.create(autenticacionUseCase.login(email, clave))
                .expectNextMatches(s -> s.getCorreoElectronico().equals(email) && s.getClave().equals(clave))
                .verifyComplete();

        verify(solicitanteRepository, times(1)).findByCorreoElectronico(email);
    }

    @Test
    void testLogin_CorreoNoExiste() {
        // Arrange
        String email = "inexistente@correo.com";
        String clave = "12345";

        when(solicitanteRepository.findByCorreoElectronico(email))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(autenticacionUseCase.login(email, clave))
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().equals("Credenciales inválidas"))
                .verify();

        verify(solicitanteRepository, times(1)).findByCorreoElectronico(email);
    }

    @Test
    void testLogin_ClaveIncorrecta() {
        // Arrange
        String email = "test@correo.com";
        String claveCorrecta = "12345";
        String claveIncorrecta = "wrong";

        Solicitante solicitante = new Solicitante();
        solicitante.setCorreoElectronico(email);
        solicitante.setClave(claveCorrecta);

        when(solicitanteRepository.findByCorreoElectronico(email))
                .thenReturn(Mono.just(solicitante));

        // Act & Assert
        StepVerifier.create(autenticacionUseCase.login(email, claveIncorrecta))
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().equals("Credenciales inválidas"))
                .verify();

        verify(solicitanteRepository, times(1)).findByCorreoElectronico(email);
    }
}
