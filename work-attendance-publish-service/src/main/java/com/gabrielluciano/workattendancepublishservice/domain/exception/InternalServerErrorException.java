package com.gabrielluciano.workattendancepublishservice.domain.exception;

import lombok.Getter;

@Getter
public class InternalServerErrorException extends RuntimeException {

    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
