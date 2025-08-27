package co.com.pragma.api.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleValidationException(ValidationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validación fallida");
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now());
        return Mono.just(ResponseEntity.badRequest().body(response));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable rootCause = ex.getCause();
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Error en formato de datos");

        if (rootCause instanceof InvalidFormatException invalidEx
                && invalidEx.getCause() instanceof DateTimeParseException dateEx) {
            body.put("message", "Formato de fecha inválido, se esperaba 'yyyy-MM-dd' o 'yyyy-MM-ddTHH:mm:ss'");
            body.put("detail", dateEx.getParsedString());
        } else {
            body.put("message", rootCause != null ? rootCause.getClass().getSimpleName() : ex.getClass().getSimpleName());
            body.put("detail", rootCause != null ? rootCause.getMessage() : ex.getMessage());
        }

        body.put("timestamp", LocalDateTime.now());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<Map<String, Object>> handleServerWebInput(ServerWebInputException ex) {
        Throwable rootCause = ex.getCause();
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Error de entrada de dato");
        body.put("message", rootCause != null ? rootCause.getClass().getSimpleName() : ex.getClass().getSimpleName());
        body.put("detail", rootCause != null ? rootCause.getMessage() : ex.getMessage());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("timestamp", LocalDateTime.now());

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleWebExchangeBindException(WebExchangeBindException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validación fallida");
        response.put("message", ex.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList());
        response.put("timestamp", LocalDateTime.now());
        return Mono.just(ResponseEntity.badRequest().body(response));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.CONFLICT.value());
        response.put("error", "Error de negocio");
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now());
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(response));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleGeneralException(Exception ex) {
        Throwable rootCause = ex.getCause();
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Error inesperado");
        response.put("message", rootCause != null ? rootCause.getClass().getSimpleName() : ex.getClass().getSimpleName());
        response.put("detail", rootCause != null ? rootCause.getMessage() : ex.getMessage());
        response.put("timestamp", LocalDateTime.now());
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));
    }


   @ExceptionHandler(AuthenticationException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleAuthException(AuthenticationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.UNAUTHORIZED.value());
        response.put("error", "No autenticado");
        response.put("message", ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response));
    }


    @ExceptionHandler(AccessDeniedException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleAccessDenied(AccessDeniedException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.FORBIDDEN.value());
        response.put("error", "Acceso denegado");
        response.put("message", "No tienes permisos para acceder a este recurso");
        return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
    }
}