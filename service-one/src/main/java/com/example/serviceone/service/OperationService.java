package com.example.serviceone.service;

import com.example.serviceone.dto.OperationCreateRequest;
import com.example.serviceone.dto.OperationDto;
import com.example.serviceone.dto.OperationResponse;
import com.example.serviceone.dto.OperationUpdateRequest;

import java.util.UUID;

public interface OperationService {
    OperationResponse createOperation(OperationCreateRequest request);
    String createOperationAsync(OperationCreateRequest request);
    OperationDto getOperation(UUID id);
    OperationResponse updateOperation(UUID id, OperationUpdateRequest request);
    OperationResponse deleteOperation(UUID id);
}