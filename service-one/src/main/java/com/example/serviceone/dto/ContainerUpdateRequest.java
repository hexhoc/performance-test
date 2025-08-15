package com.example.serviceone.dto;

import com.example.serviceone.entity.OperationTypeEnum;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder(toBuilder = true)
public record ContainerUpdateRequest(
        Long id,
        UUID operationId,
        OperationTypeEnum operationType,
        BigDecimal amount
) {
}
