package com.gabrielluciano.workattendancepublishservice.infra.exception;

public class MicroserviceCommunicationErrorException extends RuntimeException {

    public MicroserviceCommunicationErrorException(String message, Exception ex) {
        super(message, ex);
    }
}
