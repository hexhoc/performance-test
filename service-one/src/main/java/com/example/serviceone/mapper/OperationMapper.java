package com.example.serviceone.mapper;

import com.example.serviceone.dto.OperationCreateRequest;
import com.example.serviceone.dto.OperationDto;
import com.example.serviceone.entity.OperationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OperationMapper {

    public OperationDto toDetailsResponse(OperationEntity operationEntity) {
        return OperationDto.builder()
                .id(operationEntity.getId())
                .name(operationEntity.getName())
                .type(operationEntity.getType().name())
                .status(operationEntity.getStatus().name())
                .containerId(operationEntity.getContainerId())
                .amount(operationEntity.getAmount())
                .created(operationEntity.getCreated())
                .updated(operationEntity.getUpdated())
                .build();
    }


    public OperationEntity toEntity(OperationCreateRequest request, OperationEntity.OperationStatus status) {
        return OperationEntity.builder()
                .id(UUID.randomUUID())
                .name(request.name())
                .type(request.type())
                .status(status)
                .amount(request.amount())
                .deleted(false)
                .build();
    }
}
