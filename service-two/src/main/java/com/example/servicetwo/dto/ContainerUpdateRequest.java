package com.example.servicetwo.dto;

import com.example.servicetwo.entity.OperationTypeEnum;
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
