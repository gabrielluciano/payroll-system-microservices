package com.gabrielluciano.employeeservice.application.resource.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StandardError {

    private String error;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private int status;
}
