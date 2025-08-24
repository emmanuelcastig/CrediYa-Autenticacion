package co.com.pragma.api;

import co.com.pragma.api.dto.SolicitanteRequest;
import co.com.pragma.api.exception.ValidationException;
import co.com.pragma.api.mapper.SolicitanteMapper;
import co.com.pragma.usecase.solicitante.in.CrearSolicitantePort;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {

    private final CrearSolicitantePort crearSolicitantePort;
    private final Validator validator;
    private final SolicitanteMapper solicitanteMapper;
    private final TransactionalOperator transactionalOperator;

    public Mono<ServerResponse> crearSolicitante(ServerRequest serverRequest) {
        log.trace("Iniciando creación de solicitante desde request");

        return serverRequest.bodyToMono(SolicitanteRequest.class)
                .doOnNext(request -> log.debug("Payload recibido: {}", request))
                .flatMap(this::validacion)
                .doOnNext(valid -> log.trace("Payload validado correctamente"))
                .map(solicitanteMapper::toDomain)
                .doOnNext(domain -> log.debug("Objeto de dominio generado: {}", domain))
                .flatMap(solicitante -> crearSolicitantePort.crearSolicitante(solicitante)
                        .as(transactionalOperator::transactional))
                .map(solicitanteMapper::toResponse)
                .doOnSuccess(saved -> log.info("Solicitante creado exitosamente: {}", saved))
                .doOnError(error -> log.error("Error al crear solicitante", error))
                .flatMap(saved -> {
                    log.trace("Construyendo respuesta HTTP 201 para solicitante: {}", saved);
                    return ServerResponse.status(HttpStatus.CREATED).bodyValue(saved);
                });
    }

    public Mono<SolicitanteRequest> validacion(SolicitanteRequest request) {
        Set<ConstraintViolation<SolicitanteRequest>> violaciones = validator.validate(request);
        if (!violaciones.isEmpty()) {
            String errorMessage = violaciones.stream()
                    .map(violation -> violation.getPropertyPath() + ": " +
                            violation.getMessage())
                    .collect(Collectors.joining(", "));
            return Mono.error(new ValidationException(errorMessage));
        }
        return Mono.just(request);
    }

}
