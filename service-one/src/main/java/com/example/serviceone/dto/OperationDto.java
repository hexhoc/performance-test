package com.example.serviceone.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record OperationDto(
        UUID id,
        String name,
        String type,
        String status,
        Long containerId,
        BigDecimal amount,
        LocalDateTime created,
        LocalDateTime updated
) {
}