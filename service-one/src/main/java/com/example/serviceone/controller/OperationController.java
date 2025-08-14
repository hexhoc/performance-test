package com.example.serviceone.controller;

import com.example.serviceone.dto.OperationCreateRequest;
import com.example.serviceone.dto.OperationDto;
import com.example.serviceone.dto.OperationResponse;
import com.example.serviceone.dto.OperationUpdateRequest;
import com.example.serviceone.service.OperationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/operations")
@RequiredArgsConstructor
@Tag(name = "Operation Management", description = "APIs for managing operations")
public class OperationController {

    private final OperationService operationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new operation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operation created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public OperationResponse createOperation(@Valid @RequestBody OperationCreateRequest request) {
        return operationService.createOperation(request);
    }

    @PostMapping("/async")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Create a new operation asynchronously")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Async operation request accepted"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public String createOperationAsync(@Valid @RequestBody OperationCreateRequest request) {
        return operationService.createOperationAsync(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an operation by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation found"),
            @ApiResponse(responseCode = "404", description = "Operation not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public OperationDto getOperation(
            @Parameter(description = "ID of the operation to be retrieved", required = true)
            @PathVariable UUID id) {
        return operationService.getOperation(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing operation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Operation not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public OperationResponse updateOperation(
            @Parameter(description = "ID of the operation to be updated", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody OperationUpdateRequest request) {
        return operationService.updateOperation(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an operation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Operation not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public OperationResponse deleteOperation(
            @Parameter(description = "ID of the operation to be deleted", required = true)
            @PathVariable UUID id) {
        return operationService.deleteOperation(id);
    }
}