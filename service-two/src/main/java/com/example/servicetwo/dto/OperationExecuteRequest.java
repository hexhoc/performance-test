package com.example.servicetwo.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder(toBuilder = true)
public record OperationExecuteRequest(
        UUID containerId,
        UUID operationId,
        BigDecimal amount
) { }
