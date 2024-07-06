package com.example.calculateservice.handler;

import com.example.calculateservice.config.KafkaConfig;
import com.example.calculateservice.constant.EventTypeEnum;
import com.example.calculateservice.dto.CalculationDto;
import com.example.calculateservice.message.event.StepOneEndEvent;
import com.example.calculateservice.service.CalculationService;
import com.example.calculateservice.service.IncomingEventService;
import com.example.calculateservice.service.OutgoingEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StepOneEndEventHandler {
    private final IncomingEventService incomingEventService;
    private final OutgoingEventService outgoingEventService;
    private final CalculationService calculationService;
    private final ObjectMapper objectMapper;

    @EventListener
    public void handle(StepOneEndEvent stepOneEndEvent) {
        log.info("Handle event: {}", stepOneEndEvent.getClass());
        var incomingEvent = incomingEventService.createEvent(
            stepOneEndEvent.getPayload(),
            stepOneEndEvent.getTraceId(),
            stepOneEndEvent.getRequestId(),
            stepOneEndEvent.getFrom(),
            stepOneEndEvent.getEventType(),
            CalculationDto.class);
        try {
            calculationService.update(incomingEvent.getPayload());
            incomingEventService.saveWithSuccess(incomingEvent);
            outgoingEventService.createAndSend(incomingEvent, EventTypeEnum.STEP_TWO, objectMapper.writeValueAsString(incomingEvent.getPayload()), KafkaConfig.SERVICE_ONE_TOPIC);
        } catch (Exception e) {
            log.error(e.getMessage());
            incomingEventService.saveWithError(incomingEvent);
        }
    }
}
