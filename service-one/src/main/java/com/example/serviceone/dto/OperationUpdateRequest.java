package com.example.serviceone.dto;

import com.example.serviceone.entity.OperationStatusEnum;
import com.example.serviceone.entity.OperationTypeEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder(toBuilder = true)
public record OperationUpdateRequest(

        @NotNull
        UUID id,

        @NotBlank(message = "Name is required")
        @Size(max = 50, message = "Name must be less than 50 characters")
        String name,

        @NotNull(message = "Type is required")
        OperationTypeEnum type,

        OperationStatusEnum status,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
        @Digits(integer = 18, fraction = 2, message = "Amount must have up to 2 decimal places")
        BigDecimal amount
) {}