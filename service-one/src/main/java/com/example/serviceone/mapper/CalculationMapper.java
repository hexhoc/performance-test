package com.example.serviceone.mapper;

import com.example.serviceone.dto.CalculationDto;
import com.example.serviceone.entity.CalculationEntity;
import com.example.serviceone.entity.IncomingEventEntity;
import com.example.serviceone.model.IncomingEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CalculationMapper {
    private final ObjectMapper objectMapper;

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
