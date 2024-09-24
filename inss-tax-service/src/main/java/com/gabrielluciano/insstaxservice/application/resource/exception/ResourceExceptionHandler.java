package com.gabrielluciano.insstaxservice.application.resource.exception;

import com.gabrielluciano.insstaxservice.domain.exception.EntityNotFoundException;
import com.gabrielluciano.insstaxservice.domain.exception.InvalidTaxRateException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ResourceExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
                                                                               HttpServletRequest request) {
        var error = StandardError.builder()
                .error("Data Integrity Violation")
                .message(ExceptionUtils.getRootCauseMessage(ex))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        log.info("Data Integrity Violation. Error details: {}", error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

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
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
    }

    @ExceptionHandler(InvalidTaxRateException.class)
    public ResponseEntity<StandardError> handleInvalidTaxRateException(InvalidTaxRateException ex,
                                                                       HttpServletRequest request) {

        var error = StandardError.builder()
                .error("Invalid Tax Rate")
                .message(ExceptionUtils.getRootCauseMessage(ex))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        log.info("Invalid Tax Rate. Error details: {}", error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardError> handleEntityNotFoundException(EntityNotFoundException ex,
                                                                       HttpServletRequest request) {
        var error = StandardError.builder()
                .error("Entity Not Found")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .build();
        log.info("Entity Not Found. Id: {}", ex.getId());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
