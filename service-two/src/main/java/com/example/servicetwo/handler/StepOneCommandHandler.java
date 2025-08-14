package com.example.servicetwo.handler;

import com.example.servicetwo.config.KafkaConfig;
import com.example.servicetwo.constant.EventTypeEnum;
import com.example.servicetwo.message.event.StepOneCommand;
import com.example.servicetwo.service.IncomingEventService;
import com.example.servicetwo.service.OutgoingEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StepOneCommandHandler {
    private final IncomingEventService incomingEventService;
    private final OutgoingEventService outgoingEventService;
    private final CalculationService calculationService;
    private final ObjectMapper objectMapper;

    @EventListener
    public void handle(StepOneCommand stepOneCommand) {
        log.info("Handle event: STEP 1 START");
        var incomingEvent = incomingEventService.createEvent(
            stepOneCommand.getPayload(),
            stepOneCommand.getTraceId(),
            stepOneCommand.getRequestId(),
            stepOneCommand.getFrom(),
            stepOneCommand.getEventType(),
            CalculationDto.class);
        try {
            var id = incomingEvent.getPayload().getId();
            calculationService.createIfNotExist(id);
            var calculationDto = calculationService.calculateValue(id, 1);
            incomingEventService.saveWithSuccess(incomingEvent);
            outgoingEventService.createAndSend(incomingEvent, EventTypeEnum.STEP_ONE_END, objectMapper.writeValueAsString(calculationDto), KafkaConfig.SERVICE_TWO_TOPIC);
        } catch (Exception e) {
            log.error(e.getMessage());
            incomingEventService.saveWithError(incomingEvent);
        }
    }
}
