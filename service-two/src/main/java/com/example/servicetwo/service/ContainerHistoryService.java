package com.example.servicetwo.service;

import com.example.servicetwo.dto.ContainerUpdateRequest;
import com.example.servicetwo.entity.ContainerHistoryEntity;
import com.example.servicetwo.mapper.ContainerHistoryMapper;
import com.example.servicetwo.repository.ContainerHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContainerHistoryService {

    private final ContainerHistoryRepository containerHistoryRepository;
    private final ContainerHistoryMapper containerHistoryMapper;

    public ContainerHistoryEntity save(ContainerUpdateRequest request) {
        return containerHistoryRepository.save(containerHistoryMapper.toEntity(request));
    }
}
