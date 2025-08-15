package com.example.serviceone.dto;

import com.example.serviceone.entity.OperationStatusEnum;
import com.example.serviceone.entity.OperationTypeEnum;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record OperationDto(
        UUID id,
        String name,
        OperationTypeEnum type,
        OperationStatusEnum status,
        Long containerId,
        BigDecimal amount,
        LocalDateTime created,
        LocalDateTime updated
) {
}