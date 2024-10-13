package com.gabrielluciano.payrollservice.infra.exception;

public class MicroserviceCommunicationErrorException extends RuntimeException {

    public MicroserviceCommunicationErrorException(String message, Exception ex) {
        super(message, ex);
    }

    public MicroserviceCommunicationErrorException(String message) {
        super(message);
    }
}

