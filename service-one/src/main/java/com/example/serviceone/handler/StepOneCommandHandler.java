package com.example.serviceone.handler;

import com.example.serviceone.config.KafkaConfig;
import com.example.serviceone.constant.EventTypeEnum;
import com.example.serviceone.constant.SourceEnum;
import com.example.serviceone.entity.OutgoingEventEntity;
import com.example.serviceone.message.MessageSender;
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
public class StepOneCommandHandler {

    private final IncomingEventService incomingEventService;
    private final OutgoingEventService outgoingEventService;
    private final CalculationService calculationService;
    private final ObjectMapper objectMapper;

    public void handle() {
        log.info("Handle command: calculate");
        var incomingEvent = incomingEventService.createEvent(null, EventTypeEnum.STEP_ONE, SourceEnum.HTTP, Object.class);
        try {
            var calculationDto = calculationService.findAny();
            incomingEventService.saveWithSuccess(incomingEvent);
            outgoingEventService.createAndSend(incomingEvent, objectMapper.writeValueAsString(calculationDto), KafkaConfig.SERVICE_ONE_TOPIC);
        } catch (Exception e) {
            log.error(e.getMessage());
            incomingEventService.saveWithError(incomingEvent);
        }
    }
}
