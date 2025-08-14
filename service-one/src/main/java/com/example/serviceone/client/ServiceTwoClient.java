package com.example.serviceone.client;

import com.example.serviceone.dto.ContainerDto;
import com.example.serviceone.dto.ContainerUpdateRequest;
import com.example.serviceone.exception.ServiceTwoIntegrationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Set;

@Component
@Slf4j
public class ServiceTwoClient {

    private final RestClient restClient;
    private final static Set<Integer> NOT_FOUND_STATUS_CODES = Set.of(204, 404);


    public ServiceTwoClient(
            @Value("${service-two.url}") String serviceTwoUrl,
            RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl(serviceTwoUrl)
                .defaultStatusHandler(
                        HttpStatusCode::isError,
                        (request, response) -> {
                            throw new ServiceTwoIntegrationException(
                                    "Failed to call Service-Two: " + response.getStatusText());
                        })
                .build();
    }

    public ContainerDto getContainer(Long containerId) {
        return restClient.get()
                .uri("/api/v1/containers/%s".formatted(containerId.toString()))
                .retrieve()
                .onStatus(HttpStatusCode -> NOT_FOUND_STATUS_CODES.contains(HttpStatusCode.value()), (request, response) -> {
                    String errorMessage = "Container not found by id: %s".formatted(containerId);
                    log.error(errorMessage);
                    throw new ServiceTwoIntegrationException(errorMessage);
                })
                .onStatus(HttpStatusCode -> HttpStatusCode.value() != 200, (request, response) -> {
                    String errorMessage = "Failed to get container by id: %s with status code: %s and message: %s".formatted(
                            containerId,
                            response.getStatusCode(),
                            response.getStatusText()
                    );
                    log.error(errorMessage);
                    throw new ServiceTwoIntegrationException(errorMessage);
                })
                .body(ContainerDto.class);
    }

    public ContainerDto updateContainer(ContainerUpdateRequest containerUpdateRequest) {
        return restClient.put()
                .uri("/api/v1/containers")
                .body(containerUpdateRequest)
                .retrieve()
                .onStatus(HttpStatusCode -> NOT_FOUND_STATUS_CODES.contains(HttpStatusCode.value()), (request, response) -> {
                    String errorMessage = "Container not found by id: %s".formatted(containerUpdateRequest.id());
                    log.error(errorMessage);
                    throw new ServiceTwoIntegrationException(errorMessage);
                })
                .onStatus(HttpStatusCode -> HttpStatusCode.value() != 200, (request, response) -> {
                    String errorMessage = "Failed to get container by id: %s with status code: %s and message: %s".formatted(
                            containerUpdateRequest.id(),
                            response.getStatusCode(),
                            response.getStatusText()
                    );
                    log.error(errorMessage);
                    throw new ServiceTwoIntegrationException(errorMessage);
                })
                .body(ContainerDto.class);
    }
}