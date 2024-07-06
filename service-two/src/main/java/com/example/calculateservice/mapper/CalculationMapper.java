package com.example.calculateservice.mapper;

import com.example.calculateservice.dto.CalculationDto;
import com.example.calculateservice.entity.CalculationEntity;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CalculationMapper {

    @SneakyThrows
    public CalculationDto toDto(CalculationEntity entity) {
        return new CalculationDto(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getValue());

    }

    @SneakyThrows
    public CalculationEntity toEntity(CalculationDto dto) {
        return new CalculationEntity(
            dto.getId(),
            dto.getName(),
            dto.getDescription(),
            dto.getValue());
    }
}
