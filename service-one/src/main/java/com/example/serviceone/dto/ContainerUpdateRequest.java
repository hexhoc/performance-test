package com.example.serviceone.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder(toBuilder = true)
public record ContainerUpdateRequest(
        Long id,
        UUID operationId,
        String operationType,
        BigDecimal amount
) {
}
