package co.com.pragma.api;

import co.com.pragma.api.config.JwtProvider;
import co.com.pragma.api.dto.LoginRequest;
import co.com.pragma.api.dto.LoginResponse;
import co.com.pragma.api.dto.SolicitanteRequest;
import co.com.pragma.api.exception.ValidationException;
import co.com.pragma.api.mapper.SolicitanteMapper;
import co.com.pragma.usecase.in.CrearSolicitantePort;
import co.com.pragma.usecase.in.LoginSolicitantePort;
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

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {

    private final CrearSolicitantePort crearSolicitantePort;
    private final LoginSolicitantePort loginSolicitantePort;
    private final Validator validator;
    private final SolicitanteMapper solicitanteMapper;
    private final JwtProvider jwtProvider;
    private final TransactionalOperator transactionalOperator;

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(LoginRequest.class)
                .flatMap(authReq ->
                        loginSolicitantePort.login(authReq.getEmail(), authReq.getClave())
                                .doOnNext(solicitante -> log.debug("Autenticación exitosa: {}",
                                        solicitante.getCorreoElectronico()))
                                .flatMap(solicitante -> {
                                    String token = jwtProvider.generateToken(solicitante.getCorreoElectronico());
                                    var authResponse = new LoginResponse(token);
                                    return ServerResponse.ok().bodyValue(authResponse);
                                })
                                .onErrorResume(e -> {
                                    log.error("Error en el login: {}", e.getMessage());
                                    return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                                            .bodyValue(Map.of("error", "Credenciales inválidas"));
                                })
                );
    }

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

    public Mono<ServerResponse> solicitanteExistente(ServerRequest serverRequest) {
        String numeroDocumento = serverRequest.pathVariable("documento");
        log.trace("Iniciando verificacion de existencia para documento: {}", numeroDocumento);

        return crearSolicitantePort.solicitanteExiste(numeroDocumento)
                .doOnSubscribe(sub -> log.debug("Consultando existencia del solicitante con documento {}", numeroDocumento))
                .flatMap(existe -> {
                    if (Boolean.TRUE.equals(existe)) {
                        log.info("Solicitante con documento {} existe", numeroDocumento);
                        return ServerResponse.ok()
                                .bodyValue(Map.of("existe", true, "mensaje", "El solicitante existe"));
                    } else {
                        log.warn("No se encontro solicitante con documento {}", numeroDocumento);
                        return ServerResponse.status(HttpStatus.NOT_FOUND)
                                .bodyValue(Map.of("existe", false, "mensaje", "No se encontro solicitante"));
                    }
                })
                .doOnError(error -> log.error("Error verificando existencia del solicitante {}", numeroDocumento, error));
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
