package com.example.servicetwo.exception;

public class ContainerNotFoundException extends RuntimeException {
    public ContainerNotFoundException(Long id) {
        super("Container not found with id: " + id);
    }
}
