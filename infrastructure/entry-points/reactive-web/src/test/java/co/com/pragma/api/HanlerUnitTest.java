/*
package co.com.pragma.api;

import co.com.pragma.api.security.JwtProvider;
import co.com.pragma.api.dto.SolicitanteRequest;
import co.com.pragma.api.dto.SolicitanteResponse;
import co.com.pragma.api.exception.ValidationException;
import co.com.pragma.api.mapper.SolicitanteMapper;
import co.com.pragma.model.solicitante.Solicitante;
import co.com.pragma.usecase.in.CrearSolicitantePort;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerUnitTest {

    private CrearSolicitantePort crearSolicitantePort;
    private Validator validator;
    private SolicitanteMapper solicitanteMapper;
    private TransactionalOperator transactionalOperator;
    private JwtProvider jwtProvider;
    private UserDetailsRepositoryReactiveAuthenticationManager authenticationManager;
    private Handler handler;

    @BeforeEach
    void setUp() {
        crearSolicitantePort = mock(CrearSolicitantePort.class);
        validator = mock(Validator.class);
        solicitanteMapper = mock(SolicitanteMapper.class);
        transactionalOperator = mock(TransactionalOperator.class);
        handler = new Handler(crearSolicitantePort, validator, solicitanteMapper, jwtProvider, transactionalOperator,
                );
    }

    @Test
    void validacion_shouldReturnErrorOnViolation() {
        SolicitanteRequest request = new SolicitanteRequest();
        ConstraintViolation<SolicitanteRequest> violation = mock(ConstraintViolation.class);
        Path mockPath = mock(Path.class);
        when(mockPath.toString()).thenReturn("field");
        when(violation.getPropertyPath()).thenReturn(mockPath);
        when(violation.getMessage()).thenReturn("must not be null");
        when(validator.validate(request)).thenReturn(Set.of(violation));

        StepVerifier.create(handler.validacion(request))
                .expectError(ValidationException.class)
                .verify();
    }

    @Test
    void crearSolicitante_shouldReturnCreatedResponse() {
        ServerRequest serverRequest = mock(ServerRequest.class);
        SolicitanteRequest request = new SolicitanteRequest();
        Solicitante domain = new Solicitante();
        SolicitanteResponse response = new SolicitanteResponse();

        when(serverRequest.bodyToMono(SolicitanteRequest.class)).thenReturn(Mono.just(request));
        when(validator.validate(request)).thenReturn(Collections.emptySet());
        when(solicitanteMapper.toDomain(request)).thenReturn(domain);
        when(crearSolicitantePort.crearSolicitante(domain)).thenReturn(Mono.just(domain));
        when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(inv -> inv.getArgument(0));
        when(solicitanteMapper.toResponse(domain)).thenReturn(response);

        StepVerifier.create(handler.crearSolicitante(serverRequest))
                .expectNextMatches(serverResponse ->
                        serverResponse.statusCode().equals(HttpStatus.CREATED))
                .verifyComplete();
    }

    @Test
    void crearSolicitante_shouldHandleTransactionalError() {
        ServerRequest serverRequest = mock(ServerRequest.class);
        SolicitanteRequest request = new SolicitanteRequest();
        when(serverRequest.bodyToMono(SolicitanteRequest.class)).thenReturn(Mono.just(request));
        when(validator.validate(request)).thenReturn(Collections.emptySet());
        Solicitante domain = new Solicitante();
        when(solicitanteMapper.toDomain(request)).thenReturn(domain);
        when(crearSolicitantePort.crearSolicitante(domain)).thenReturn(Mono.error(new RuntimeException("DB error")));
        when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(inv -> inv.getArgument(0));

        StepVerifier.create(handler.crearSolicitante(serverRequest))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void solicitanteExistente_shouldReturnOkWhenExists() {
        ServerRequest serverRequest = mock(ServerRequest.class);
        when(serverRequest.pathVariable("documento")).thenReturn("123");
        when(crearSolicitantePort.solicitanteExiste("123")).thenReturn(Mono.just(true));

        StepVerifier.create(handler.solicitanteExistente(serverRequest))
                .expectNextMatches(response ->
                        response.statusCode().equals(HttpStatus.OK))
                .verifyComplete();
    }

    @Test
    void solicitanteExistente_shouldReturnNotFoundWhenNotExists() {
        ServerRequest serverRequest = mock(ServerRequest.class);
        when(serverRequest.pathVariable("documento")).thenReturn("999");
        when(crearSolicitantePort.solicitanteExiste("999")).thenReturn(Mono.just(false));

        StepVerifier.create(handler.solicitanteExistente(serverRequest))
                .expectNextMatches(response ->
                        response.statusCode().equals(HttpStatus.NOT_FOUND))
                .verifyComplete();
    }

    @Test
    void solicitanteExistente_shouldReturnErrorOnFailure() {
        ServerRequest serverRequest = mock(ServerRequest.class);
        when(serverRequest.pathVariable("documento")).thenReturn("500");
        when(crearSolicitantePort.solicitanteExiste("500"))
                .thenReturn(Mono.error(new RuntimeException("DB error")));

        StepVerifier.create(handler.solicitanteExistente(serverRequest))
                .expectError(RuntimeException.class)
                .verify();
    }
}
*/
