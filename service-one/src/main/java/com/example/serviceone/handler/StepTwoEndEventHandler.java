package com.example.serviceone.handler;

import com.example.serviceone.config.KafkaConfig;
import com.example.serviceone.constant.EventTypeEnum;
import com.example.serviceone.dto.CalculationDto;
import com.example.serviceone.message.event.StepOneEndEvent;
import com.example.serviceone.message.event.StepTwoEndEvent;
import com.example.serviceone.service.CalculationService;
import com.example.serviceone.service.IncomingEventService;
import com.example.serviceone.service.OutgoingEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StepTwoEndEventHandler {
    private final IncomingEventService incomingEventService;
    private final OutgoingEventService outgoingEventService;
    private final CalculationService calculationService;
    private final ObjectMapper objectMapper;

    @EventListener
    public void handle(StepTwoEndEvent stepTwoEndEvent) {
        log.info("Handle event: {}", stepTwoEndEvent.getClass());
        var incomingEvent = incomingEventService.createEvent(
            stepTwoEndEvent.getPayload(),
            stepTwoEndEvent.getTraceId(),
            stepTwoEndEvent.getRequestId(),
            stepTwoEndEvent.getFrom(),
            stepTwoEndEvent.getEventType(),
            CalculationDto.class);
        try {
            calculationService.update(incomingEvent.getPayload());
            incomingEventService.saveWithSuccess(incomingEvent);
            outgoingEventService.createAndSend(incomingEvent, EventTypeEnum.STEP_THREE, objectMapper.writeValueAsString(incomingEvent.getPayload()), KafkaConfig.SERVICE_ONE_TOPIC);
        } catch (Exception e) {
            log.error(e.getMessage());
            incomingEventService.saveWithError(incomingEvent);
        }
    }
}
