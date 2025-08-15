package com.example.servicetwo.service;

import com.example.servicetwo.dto.ContainerCreateRequest;
import com.example.servicetwo.dto.ContainerDto;
import com.example.servicetwo.dto.ContainerUpdateRequest;
import com.example.servicetwo.entity.ContainerEntity;
import com.example.servicetwo.entity.OperationTypeEnum;
import com.example.servicetwo.exception.ContainerNotFoundException;
import com.example.servicetwo.mapper.ContainerMapper;
import com.example.servicetwo.repository.ContainerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ContainerService {

    private final ContainerHistoryService containerHistoryService;
    private final ContainerRepository containerRepository;
    private final ContainerMapper containerMapper;

    public ContainerDto getById(Long id) {
        ContainerEntity container = containerRepository.findById(id)
                .orElseThrow(() -> new ContainerNotFoundException(id));
        return containerMapper.toDto(container);
    }

    @Transactional
    public ContainerDto create(ContainerCreateRequest request) {
        ContainerEntity container = containerMapper.toEntity(request);
        container.setDeleted(false);
        container.setLocked(false);
        ContainerEntity savedContainer = containerRepository.save(container);
        return containerMapper.toDto(savedContainer);
    }

    @Transactional
    public ContainerDto update(ContainerUpdateRequest request) {
        ContainerEntity container = containerRepository.findByIdForUpdate(request.id())
                .orElseThrow(() -> new ContainerNotFoundException(request.id()));

        BigDecimal operationAmount = request.operationType().equals(OperationTypeEnum.DECREASE)
                ? request.amount().negate()
                : request.amount();

        container.setAmount(container.getAmount().add(operationAmount));
        if (container.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Container amount cannot be negative after operation"
            );
        }

        ContainerEntity updatedContainer = containerRepository.save(container);
        containerHistoryService.save(request);

        return containerMapper.toDto(updatedContainer);
    }

    @Transactional
    public void deleteById(Long id) {
        ContainerEntity container = containerRepository.findById(id)
                .orElseThrow(() -> new ContainerNotFoundException(id));
        container.setDeleted(true);
        containerRepository.save(container);
    }

    @Transactional
    public void lockContainer(Long id) {
        ContainerEntity container = containerRepository.findById(id)
                .orElseThrow(() -> new ContainerNotFoundException(id));
        container.setLocked(true);
        containerRepository.save(container);
    }

    @Transactional
    public void unlockContainer(Long id) {
        ContainerEntity container = containerRepository.findById(id)
                .orElseThrow(() -> new ContainerNotFoundException(id));
        container.setLocked(false);
        containerRepository.save(container);
    }

}
