package com.example.transactionalbox.model;

import com.example.transactionalbox.enumeration.IdempotencyLevelEnum;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record IdempotencyInfo(
        UUID existingIncomingEventId,
        int version,
        IdempotencyLevelEnum level
)
{ }