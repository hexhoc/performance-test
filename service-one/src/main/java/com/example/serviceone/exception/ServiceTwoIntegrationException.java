package com.example.serviceone.exception;

public class ServiceTwoIntegrationException extends RuntimeException {
    public ServiceTwoIntegrationException(String message) {
        super(message);
    }

    public ServiceTwoIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}