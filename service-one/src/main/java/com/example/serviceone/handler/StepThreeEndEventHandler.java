package com.example.serviceone.handler;

import com.example.serviceone.config.KafkaConfig;
import com.example.serviceone.constant.EventTypeEnum;
import com.example.serviceone.dto.CalculationDto;
import com.example.serviceone.message.event.StepThreeEndEvent;
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
public class StepThreeEndEventHandler {
    private final IncomingEventService incomingEventService;
    private final CalculationService calculationService;

    @EventListener
    public void handle(StepThreeEndEvent stepThreeEndEvent) {
        log.info("Handle event: {}", stepThreeEndEvent.getClass());
        var incomingEvent = incomingEventService.createEvent(
            stepThreeEndEvent.getPayload(),
            stepThreeEndEvent.getTraceId(),
            stepThreeEndEvent.getRequestId(),
            stepThreeEndEvent.getFrom(),
            stepThreeEndEvent.getEventType(),
            CalculationDto.class);
        try {
            calculationService.update(incomingEvent.getPayload());
            incomingEventService.saveWithSuccess(incomingEvent);
        } catch (Exception e) {
            log.error(e.getMessage());
            incomingEventService.saveWithError(incomingEvent);
        }
    }
}
