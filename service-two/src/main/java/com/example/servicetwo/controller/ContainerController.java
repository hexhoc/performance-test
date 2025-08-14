package com.example.servicetwo.controller;

import com.example.servicetwo.dto.ContainerCreateRequest;
import com.example.servicetwo.dto.ContainerDto;
import com.example.servicetwo.dto.ContainerUpdateRequest;
import com.example.servicetwo.service.ContainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/containers")
@RequiredArgsConstructor
@Tag(name = "Container Management", description = "Endpoints for managing containers")
public class ContainerController {
    private final ContainerService containerService;
    
    @GetMapping("/{id}")
    @Operation(summary = "Get container by ID", 
               responses = {
                   @ApiResponse(responseCode = "200", description = "Container found"),
                   @ApiResponse(responseCode = "404", description = "Container not found")
               })
    public ContainerDto getContainer(@PathVariable Long id) {
        return containerService.getContainer(id);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new container")
    public ContainerDto createContainer(@Valid @RequestBody ContainerCreateRequest request) {
        return containerService.createContainer(request);
    }
    
    @PutMapping
    @Operation(summary = "Update container by ID",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Container updated"),
                   @ApiResponse(responseCode = "404", description = "Container not found")
               })
    public ContainerDto updateContainer(@Valid @RequestBody ContainerUpdateRequest request) {
        return containerService.updateContainer(request);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete container by ID",
               responses = {
                   @ApiResponse(responseCode = "204", description = "Container deleted"),
                   @ApiResponse(responseCode = "404", description = "Container not found")
               })
    public void deleteContainer(@PathVariable Long id) {
        containerService.deleteContainer(id);
    }
}