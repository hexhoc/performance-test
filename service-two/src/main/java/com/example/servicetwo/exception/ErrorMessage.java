package com.example.servicetwo.exception;

import lombok.Builder;

@Builder(toBuilder = true)
public record ErrorMessage(Integer errorCode, String errorMessage)
{ }
