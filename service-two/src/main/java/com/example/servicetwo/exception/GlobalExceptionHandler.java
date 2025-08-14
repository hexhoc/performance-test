package com.example.servicetwo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // TODO: Create library for common exception handler
    @ExceptionHandler(ContainerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleContainerNotFound(ContainerNotFoundException ex) {
        return new ErrorMessage(ErrorCodeValue.BUSINESS_ERROR, ex.getMessage());
    }
    
    @ExceptionHandler(ContainerLockedException.class)
    @ResponseStatus(HttpStatus.LOCKED)
    public ErrorMessage handleContainerLocked(ContainerLockedException ex) {
        return new ErrorMessage(ErrorCodeValue.BUSINESS_ERROR, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleInternalError(Exception ex) {
        return new ErrorMessage(ErrorCodeValue.SERVICE_ERROR, ex.getMessage());
    }
}