package com.example.calculateservice.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalculationDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal value1;
    private BigDecimal value2;
    private BigDecimal value3;
}
