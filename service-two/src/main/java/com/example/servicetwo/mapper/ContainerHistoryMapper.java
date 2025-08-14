package com.example.servicetwo.mapper;

import com.example.servicetwo.dto.ContainerUpdateRequest;
import com.example.servicetwo.entity.ContainerHistoryEntity;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component

public class ContainerHistoryMapper {

    @SneakyThrows
    public ContainerHistoryEntity toEntity(ContainerUpdateRequest request) {
        return ContainerHistoryEntity.builder()
                .id(UUID.randomUUID())
                .operationId(request.operationId())
                .operationType(request.operationType())
                .amount(request.amount())
                .build();
    }
}
