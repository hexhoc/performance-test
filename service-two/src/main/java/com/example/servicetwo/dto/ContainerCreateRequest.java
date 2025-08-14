package com.example.servicetwo.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder(toBuilder = true)
public record ContainerCreateRequest(
        String name,
        BigDecimal amount
) {
}
