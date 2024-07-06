package com.example.serviceone.controller;

import com.example.serviceone.handler.StepOneCommandHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class CalculationController {
    private final StepOneCommandHandler stepOneCommandHandler;
    @GetMapping("/calculate")
    public ResponseEntity<String> getCalculate(){
        stepOneCommandHandler.handle();
        return ResponseEntity.ok("ok");
    }
}
