package com.example.serviceone.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record OperationResponse(
        String message,
        OperationDto operation

) {}

