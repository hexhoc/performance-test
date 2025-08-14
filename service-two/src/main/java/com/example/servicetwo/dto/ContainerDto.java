package com.example.servicetwo.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record ContainerDto(
        Long id,
        String name,
        BigDecimal amount,
        Boolean locked,
        LocalDateTime created,
        LocalDateTime updated) { }
