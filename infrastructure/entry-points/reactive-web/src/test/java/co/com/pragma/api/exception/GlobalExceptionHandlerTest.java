package co.com.pragma.api.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleValidationException() {
        ValidationException ex = new ValidationException("Error de validación");

        StepVerifier.create(handler.handleValidationException(ex))
                .assertNext(r -> {
                    assertThat(r.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                    assertThat(r.getBody()).containsEntry("message", "Error de validación");
                })
                .verifyComplete();
    }


    @Test
    void handleServerWebInput() {
        ServerWebInputException ex = new ServerWebInputException("entrada mala");

        ResponseEntity<Map<String, Object>> response = handler.handleServerWebInput(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("error", "Error de entrada de dato");
    }

    @Test
    void handleWebExchangeBindException() {
        WebExchangeBindException ex = mock(WebExchangeBindException.class);

        StepVerifier.create(handler.handleWebExchangeBindException(ex))
                .assertNext(r -> {
                    assertThat(r.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                    assertThat(r.getBody()).containsEntry("error", "Validación fallida");
                })
                .verifyComplete();
    }

    @Test
    void handleIllegalArgument() {
        IllegalArgumentException ex = new IllegalArgumentException("Correo duplicado");

        StepVerifier.create(handler.handleIllegalArgument(ex))
                .assertNext(r -> {
                    assertThat(r.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
                    assertThat(r.getBody()).containsEntry("message", "Correo duplicado");
                })
                .verifyComplete();
    }

    @Test
    void handleGeneralException() {
        RuntimeException ex = new RuntimeException("error inesperado");

        StepVerifier.create(handler.handleGeneralException(ex))
                .assertNext(r -> {
                    assertThat(r.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
                    assertThat(r.getBody()).containsEntry("error", "Error inesperado");
                })
                .verifyComplete();
    }
}
