package br.ifsp.hospital.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    private ResponseEntity<ApiException> build(HttpStatus status, Exception e) {
        return ResponseEntity.status(status).body(
                ApiException.builder()
                        .status(status)
                        .message(e.getMessage())
                        .developerMessage(e.getClass().getName())
                        .timestamp(ZonedDateTime.now(ZoneId.of("Z")))
                        .build()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiException> handleIllegalArgument(IllegalArgumentException e) {
        return build(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiException> handleIllegalState(IllegalStateException e) {
        return build(HttpStatus.UNPROCESSABLE_ENTITY, e);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiException> handleNotFound(EntityNotFoundException e) {
        return build(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ApiException> handleAlreadyExists(EntityAlreadyExistsException e) {
        return build(HttpStatus.CONFLICT, e);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiException> handleNullPointer(NullPointerException e) {
        return build(HttpStatus.BAD_REQUEST, e);
    }
}
