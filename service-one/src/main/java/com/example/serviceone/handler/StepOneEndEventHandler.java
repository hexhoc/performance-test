package com.example.serviceone.handler;

import com.example.serviceone.config.KafkaConfig;
import com.example.serviceone.dto.CalculationDto;
import com.example.serviceone.entity.OutgoingEventEntity;
import com.example.serviceone.message.MessageSender;
import com.example.serviceone.message.event.StepOneEndEvent;
import com.example.serviceone.service.CalculationService;
import com.example.serviceone.service.IncomingEventService;
import com.example.serviceone.service.OutgoingEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StepOneEndEventHandler {
    private final IncomingEventService incomingEventService;
    private final OutgoingEventService outgoingEventService;
    private final CalculationService calculationService;
    private final MessageSender messageSender;
    private final ObjectMapper objectMapper;
    
    public void handle(StepOneEndEvent stepOneEndEvent) {
        log.info("Handle event: step ");
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
            outgoingEventService.createAndSend(incomingEvent, objectMapper.writeValueAsString(incomingEvent.getPayload()), KafkaConfig.SERVICE_ONE_TOPIC);
        } catch (Exception e) {
            log.error(e.getMessage());
            incomingEventService.saveWithError(incomingEvent);
        }
    }
}
