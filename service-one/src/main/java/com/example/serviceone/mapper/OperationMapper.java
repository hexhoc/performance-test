package com.example.serviceone.mapper;

import com.example.serviceone.dto.ContainerUpdateRequest;
import com.example.serviceone.dto.OperationCreateRequest;
import com.example.serviceone.dto.OperationDto;
import com.example.serviceone.entity.OperationEntity;
import com.example.serviceone.entity.OperationStatusEnum;
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
                .type(operationEntity.getType())
                .status(operationEntity.getStatus())
                .containerId(operationEntity.getContainerId())
                .amount(operationEntity.getAmount())
                .created(operationEntity.getCreated())
                .updated(operationEntity.getUpdated())
                .build();
    }

    public ContainerUpdateRequest toContainerUpdateRequest(OperationEntity entity) {
        return ContainerUpdateRequest.builder()
                .id(entity.getContainerId())
                .operationId(entity.getId())
                .operationType(entity.getType())
                .amount(entity.getAmount())
                .build();
    }


    public OperationEntity toEntity(OperationCreateRequest request, OperationStatusEnum status) {
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
