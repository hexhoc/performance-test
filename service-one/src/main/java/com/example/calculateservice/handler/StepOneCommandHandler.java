package com.example.calculateservice.handler;

import com.example.calculateservice.config.KafkaConfig;
import com.example.calculateservice.constant.EventTypeEnum;
import com.example.calculateservice.constant.SourceEnum;
import com.example.calculateservice.dto.CalculationDto;
import com.example.calculateservice.service.CalculationService;
import com.example.calculateservice.service.IncomingEventService;
import com.example.calculateservice.service.OutgoingEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StepOneCommandHandler {

    private final IncomingEventService incomingEventService;
    private final OutgoingEventService outgoingEventService;
    private final CalculationService calculationService;
    private final ObjectMapper objectMapper;

    public CalculationDto handle(Long id) {
        log.info("Handle command: calculate");
        var incomingEvent = incomingEventService.createEvent(null, EventTypeEnum.STEP_ONE, SourceEnum.HTTP, Object.class);
        try {
            var calculationDto = calculationService.findById(id);
            incomingEventService.saveWithSuccess(incomingEvent);
            outgoingEventService.createAndSend(incomingEvent, EventTypeEnum.STEP_ONE, objectMapper.writeValueAsString(calculationDto), KafkaConfig.SERVICE_ONE_TOPIC);
        
            return calculationDto;
        } catch (Exception e) {
            incomingEventService.saveWithError(incomingEvent);
            throw new RuntimeException(e);
        }
    }
}
