package com.example.servicetwo.handler;

import com.example.servicetwo.config.KafkaConfig;
import com.example.servicetwo.constant.EventTypeEnum;
import com.example.servicetwo.message.event.StepThreeCommand;
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
public class StepThreeCommandHandler {
    private final IncomingEventService incomingEventService;
    private final OutgoingEventService outgoingEventService;
    private final CalculationService calculationService;
    private final ObjectMapper objectMapper;

    @EventListener
    public void handle(StepThreeCommand stepThreeCommand) {
        log.info("Handle event: STEP 3 START");
        var incomingEvent = incomingEventService.createEvent(
            stepThreeCommand.getPayload(),
            stepThreeCommand.getTraceId(),
            stepThreeCommand.getRequestId(),
            stepThreeCommand.getFrom(),
            stepThreeCommand.getEventType(),
            CalculationDto.class);
        try {
            var id = incomingEvent.getPayload().getId();
            var calculationDto = calculationService.calculateValue(id, 3);
            incomingEventService.saveWithSuccess(incomingEvent);
            outgoingEventService.createAndSend(incomingEvent, EventTypeEnum.STEP_THREE_END, objectMapper.writeValueAsString(calculationDto), KafkaConfig.SERVICE_TWO_TOPIC);
        } catch (Exception e) {
            log.error(e.getMessage());
            incomingEventService.saveWithError(incomingEvent);
        }
    }
}
