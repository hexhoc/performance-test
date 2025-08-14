package com.example.serviceone.service.impl;

import com.example.serviceone.dto.OperationCreateRequest;
import com.example.serviceone.dto.OperationDto;
import com.example.serviceone.dto.OperationResponse;
import com.example.serviceone.dto.OperationUpdateRequest;
import com.example.serviceone.entity.OperationEntity;
import com.example.serviceone.exception.OperationException;
import com.example.serviceone.mapper.OperationMapper;
import com.example.serviceone.repository.OperationRepository;
import com.example.serviceone.service.ContainerService;
import com.example.serviceone.service.OperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OperationServiceImpl implements OperationService {

    private final OperationRepository operationRepository;
    private final OperationMapper operationMapper;
    private final ContainerService containerService;

    @Override
    @Transactional
    public OperationResponse createOperation(OperationCreateRequest request) {
        OperationEntity operationEntity = operationMapper.toEntity(request, OperationEntity.OperationStatus.CREATED);

        // send request to update container

        // Operation has three statuses:
        // 1. CREATED - use only in async operation
        // 2. COMPLETED - final status
        // 3. FAILED - final status

        try {
            containerService.update(operationEntity);
            operationEntity.setStatus(OperationEntity.OperationStatus.COMPLETED);
        } catch (Exception e) {
            log.error("Operation failed", e);
            operationEntity.setStatus(OperationEntity.OperationStatus.FAILED);
        }

        OperationEntity savedOperationEntity = operationRepository.save(operationEntity);
        String message = savedOperationEntity.getStatus().equals(OperationEntity.OperationStatus.COMPLETED)
                ? "Operation created successfully"
                : "Operation created failed";

        return new OperationResponse(
                message,
                operationMapper.toDetailsResponse(savedOperationEntity)
        );
    }

    @Override
    public String createOperationAsync(OperationCreateRequest request) {
        OperationEntity operationEntity = operationMapper.toEntity(request, OperationEntity.OperationStatus.PENDING);
        OperationEntity savedOperationEntity = operationRepository.save(operationEntity);

        // In a real implementation, we would also add to an outbox table here
        return "Async operation request received. Processing ID: " + savedOperationEntity.getId();
    }

    @Override
    public OperationDto getOperation(UUID id) {
        OperationEntity operationEntity = operationRepository.findById(id)
                .orElseThrow(() -> new OperationException("Operation not found with id: " + id));
        return operationMapper.toDetailsResponse(operationEntity);
    }

    @Override
    @Transactional
    public OperationResponse updateOperation(UUID id, OperationUpdateRequest request) {
        OperationEntity operationEntity = operationRepository.findById(id)
                .orElseThrow(() -> new OperationException("Operation not found with id: " + id));

        operationEntity.setName(request.name());
        operationEntity.setType(request.type());
        operationEntity.setAmount(request.amount());

        OperationEntity updatedOperationEntity = operationRepository.save(operationEntity);

        return new OperationResponse(
                "Operation updated successfully",
                operationMapper.toDetailsResponse(updatedOperationEntity)
        );
    }

    @Override
    @Transactional
    public OperationResponse deleteOperation(UUID id) {
        OperationEntity operationEntity = operationRepository.findById(id)
                .orElseThrow(() -> new OperationException("Operation not found with id: " + id));

        operationEntity.setDeleted(true);
        operationRepository.save(operationEntity);

        return new OperationResponse(
                "Operation deleted successfully",
                operationMapper.toDetailsResponse(operationEntity)
        );
    }

}