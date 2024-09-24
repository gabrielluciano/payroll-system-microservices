package com.gabrielluciano.insstaxservice.domain.exception;

public class InvalidTaxRateException extends RuntimeException {

    public InvalidTaxRateException(String message) {
        super(message);
    }
}
