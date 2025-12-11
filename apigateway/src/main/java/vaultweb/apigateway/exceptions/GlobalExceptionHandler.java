package vaultweb.apigateway.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.util.stream.Collectors;

/**
 * Global exception handler for the API Gateway.
 * Catches and processes various exceptions, returning appropriate HTTP responses.
 *
 * @author Calvin Shio
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(JsonProcessingException.class)
    Mono<ResponseEntity<ErrorResponse>> handleJsonProcessing(JsonProcessingException ex, ServerHttpRequest request) {
        log.error(ex.getMessage(), ex);
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ErrorResponse(
                                ex.getOriginalMessage(),
                                new Timestamp(System.currentTimeMillis()),
                                request.getURI().getPath(),
                                HttpStatus.BAD_REQUEST.toString()
                        )
                ));
    }

    @ExceptionHandler(DefaultException.class)
    Mono<ResponseEntity<ErrorResponse>> handleDefaultException(DefaultException ex, ServerHttpRequest request) {
        log.error(ex.getMessage(), ex);
        HttpStatus status = mapExceptionLevelToHttpStatus(ex.getLevel());
        return Mono.just(ResponseEntity
                .status(status)
                .body(
                        new ErrorResponse(
                                ex.getMessage(),
                                new Timestamp(System.currentTimeMillis()),
                                request.getURI().getPath(),
                                status.toString()
                        )
                ));
    }

    /**
     * Maps DefaultException level to appropriate HttpStatus.
     */
    private HttpStatus mapExceptionLevelToHttpStatus(String level) {
        if (level == null) {
            return HttpStatus.BAD_REQUEST;
        }
        switch (level) {
            case "AUTHENTICATION_EXCEPTION":
                return HttpStatus.UNAUTHORIZED;
            case "TIMEOUT_EXCEPTION":
                return HttpStatus.GATEWAY_TIMEOUT;
            case "HTTP_ERROR_EXCEPTION":
                return HttpStatus.BAD_GATEWAY;
            default:
                return HttpStatus.BAD_REQUEST;
        }
    }
    @ExceptionHandler(ConstraintViolationException.class)
    Mono<ResponseEntity<ErrorResponse>> handleConstraintViolation(ConstraintViolationException ex, ServerHttpRequest request) {
        log.error(ex.getMessage(), ex);
        String message = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));

        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new ErrorResponse(
                                message,
                                new Timestamp(System.currentTimeMillis()),
                                request.getURI().getPath(),
                                HttpStatus.BAD_REQUEST.toString()
                        )
                ));
    }

    @ExceptionHandler(Exception.class)
    Mono<ResponseEntity<ErrorResponse>> handleGeneric(Exception ex, ServerHttpRequest request) {
        log.error(ex.getMessage(), ex);
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(
                                ex.getMessage(),
                                new Timestamp(System.currentTimeMillis()),
                                request.getURI().getPath(),
                                HttpStatus.INTERNAL_SERVER_ERROR.toString()
                        )
                ));
    }

    record ErrorResponse(
            String message,
            Timestamp timestamp,
            String path,
            String errorCode
    ) {
    }
}
