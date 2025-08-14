package com.example.servicetwo.exception;

import java.util.UUID;

public class ContainerLockedException extends RuntimeException {
    public ContainerLockedException(UUID id) {
        super("Container is locked with id: " + id);
    }
}