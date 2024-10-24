package com.gabrielluciano.payrollservice.application.resource.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gabrielluciano.payrollservice.domain.service.exception.EntityNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ResourceExceptionHandler {

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
