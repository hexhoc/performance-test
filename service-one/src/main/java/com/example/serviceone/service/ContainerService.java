package com.example.serviceone.service;

import com.example.serviceone.client.ServiceTwoClient;
import com.example.serviceone.dto.ContainerDto;
import com.example.serviceone.dto.ContainerUpdateRequest;
import com.example.serviceone.exception.ServiceTwoIntegrationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Service to interact with the container service (service two)
 * NOTICE:
 * Almost all the business logic for interacting with the container has been moved here in order to demonstrate
 * the possible complexity when interacting between services.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ContainerService {
    private final ServiceTwoClient serviceTwoClient;

    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 1000))
    public ContainerDto getById(Long containerId) {
        ContainerDto containerDto = serviceTwoClient.getById(containerId);
        if (Objects.isNull(containerDto)) {
            String errorMessage = "Container not found by id: %s".formatted(containerId);
            log.error(errorMessage);
            throw new ServiceTwoIntegrationException(errorMessage);
        }

        return containerDto;
    }

    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 1000))
    public ContainerDto update(ContainerUpdateRequest containerUpdateRequest) {
        return serviceTwoClient.update(containerUpdateRequest);
    }

}
