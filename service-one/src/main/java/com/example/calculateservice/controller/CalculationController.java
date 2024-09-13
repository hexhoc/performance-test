package com.example.calculateservice.controller;

import com.example.calculateservice.dto.CalculationDto;
import com.example.calculateservice.handler.StepOneCommandHandler;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CalculationController {
    private final StepOneCommandHandler stepOneCommandHandler;


    @GetMapping("/calculate")
    @Timed(value = "calculation.time", description = "Time taken to start calculation")
    public ResponseEntity<CalculationDto> getCalculate(@RequestParam("id") Long id){
        var calculationDto = stepOneCommandHandler.handle(id);
        return ResponseEntity.ok(calculationDto);
    }
}
