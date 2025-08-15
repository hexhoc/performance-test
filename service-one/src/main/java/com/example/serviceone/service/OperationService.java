package com.example.serviceone.service;

import com.example.serviceone.dto.ContainerUpdateRequest;
import com.example.serviceone.dto.OperationCreateRequest;
import com.example.serviceone.dto.OperationDto;
import com.example.serviceone.dto.OperationResponse;
import com.example.serviceone.dto.OperationUpdateRequest;
import com.example.serviceone.entity.OperationEntity;
import com.example.serviceone.entity.OperationStatusEnum;
import com.example.serviceone.exception.OperationException;
import com.example.serviceone.handler.StepOneCommandHandler;
import com.example.serviceone.mapper.OperationMapper;
import com.example.serviceone.repository.OperationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OperationService {

    private final OperationRepository operationRepository;
    private final OperationMapper operationMapper;
    private final ContainerService containerService;
    private final StepOneCommandHandler stepOneCommandHandler;

    @Transactional
    public OperationResponse create(OperationCreateRequest request) {
        OperationEntity operationEntity = operationMapper.toEntity(request, OperationStatusEnum.CREATED);

        try {
            ContainerUpdateRequest containerUpdateRequest = operationMapper.toContainerUpdateRequest(operationEntity);
            containerService.update(containerUpdateRequest);
            operationEntity.setStatus(OperationStatusEnum.COMPLETED);
        } catch (Exception e) {
            log.error("Operation failed", e);
            operationEntity.setStatus(OperationStatusEnum.FAILED);
        }

        OperationEntity savedOperationEntity = operationRepository.save(operationEntity);
        String message = savedOperationEntity.getStatus().equals(OperationStatusEnum.COMPLETED)
                ? "Operation created successfully"
                : "Operation created failed";

        return new OperationResponse(
                message,
                operationMapper.toDetailsResponse(savedOperationEntity)
        );
    }

    public OperationResponse createAsync(OperationCreateRequest request) {
        OperationEntity operationEntity = operationMapper.toEntity(request, OperationStatusEnum.CREATED);
        OperationEntity savedOperationEntity = operationRepository.save(operationEntity);

        ContainerUpdateRequest containerUpdateRequest = operationMapper.toContainerUpdateRequest(operationEntity);
        stepOneCommandHandler.handle(containerUpdateRequest);

        return new OperationResponse(
                "Async operation request received. Processing ID: " + savedOperationEntity.getId(),
                operationMapper.toDetailsResponse(savedOperationEntity));
    }

    public OperationDto getById(UUID id) {
        OperationEntity operationEntity = operationRepository.findById(id)
                .orElseThrow(() -> new OperationException("Operation not found with id: " + id));
        return operationMapper.toDetailsResponse(operationEntity);
    }

    @Transactional
    public OperationResponse update(OperationUpdateRequest request) {
        UUID id = request.id();
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

    @Transactional
    public OperationResponse updateStatus(UUID id, OperationStatusEnum operationStatus) {
        OperationEntity operationEntity = operationRepository.findById(id)
                .orElseThrow(() -> new OperationException("Operation not found with id: " + id));

        operationEntity.setStatus(operationStatus);

        OperationEntity updatedOperationEntity = operationRepository.save(operationEntity);

        return new OperationResponse(
                "Operation updated successfully",
                operationMapper.toDetailsResponse(updatedOperationEntity)
        );
    }

    @Transactional
    public OperationResponse deleteById(UUID id) {
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