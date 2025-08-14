package com.example.servicetwo.mapper;

import com.example.servicetwo.dto.ContainerCreateRequest;
import com.example.servicetwo.dto.ContainerDto;
import com.example.servicetwo.entity.ContainerEntity;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class ContainerMapper {

    @SneakyThrows
    public ContainerDto toDto(ContainerEntity entity) {
        return ContainerDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .updated(entity.getUpdated())
                .created(entity.getCreated())
                .locked(entity.getLocked())
                .amount(entity.getAmount())
                .build();
    }

    @SneakyThrows
    public ContainerEntity toEntity(ContainerCreateRequest request) {
        return ContainerEntity.builder()
                .name(request.name())
                .amount(request.amount())
                .deleted(false)
                .locked(false)
                .build();
    }
}
