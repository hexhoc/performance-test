package com.example.serviceone.exception;

public class OperationException extends RuntimeException {
    public OperationException(String message) {
        super(message);
    }
}