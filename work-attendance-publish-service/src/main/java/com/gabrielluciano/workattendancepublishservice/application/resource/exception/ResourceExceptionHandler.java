package com.gabrielluciano.workattendancepublishservice.application.resource.exception;

import com.gabrielluciano.workattendancepublishservice.domain.exception.InternalServerErrorException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ResourceExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<StandardError> handleConstraintViolationException(ConstraintViolationException ex,
                                                                            HttpServletRequest request) {
        var error = StandardError.builder()
                .error("Constraint Violation")
                .message(createConstraintViolationsMessage(ex))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        log.info("Constraint Violation. Error details: {}", error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    private String createConstraintViolationsMessage(ConstraintViolationException ex) {
        return ex.getConstraintViolations().stream()
                .map(violation -> {
                    if (violation.getPropertyPath().toString().isBlank())
                        return violation.getMessage();
                    return violation.getPropertyPath() + ": " + violation.getMessage();
                })
                .collect(Collectors.joining(", "));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                                               HttpServletRequest request) {
        var error = StandardError.builder()
                .error("Constraint Violation")
                .message(createConstraintsViolationsMessage(ex))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        log.info("Method Argument Violation. Error details: {}", error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    private String createConstraintsViolationsMessage(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    if (error instanceof FieldError fieldError)
                        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
                    return error.getDefaultMessage();
                })
                .collect(Collectors.joining(", "));
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<StandardError> handleInternalServerErrorException(InternalServerErrorException ex,
                                                                            HttpServletRequest request) {
        var error = StandardError.builder()
                .error("Internal Server Error")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
        log.error("Internal Server Error", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}