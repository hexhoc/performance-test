package com.example.serviceone.dto;

import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record ContainerUpdateResponse(
        Integer errorCode,
        String errorMessage,
        UUID operationId,
        Long containerId
) {
}
