package com.example.calculateservice.handler;

import com.example.calculateservice.config.KafkaConfig;
import com.example.calculateservice.constant.EventTypeEnum;
import com.example.calculateservice.dto.CalculationDto;
import com.example.calculateservice.message.event.StepThreeCommand;
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
public class StepThreeCommandHandler {
    private final IncomingEventService incomingEventService;
    private final OutgoingEventService outgoingEventService;
    private final CalculationService calculationService;
    private final ObjectMapper objectMapper;

    @EventListener
    public void handle(StepThreeCommand stepThreeCommand) {
        log.info("Handle event: {}", stepThreeCommand.getClass());
        var incomingEvent = incomingEventService.createEvent(
            stepThreeCommand.getPayload(),
            stepThreeCommand.getTraceId(),
            stepThreeCommand.getRequestId(),
            stepThreeCommand.getFrom(),
            stepThreeCommand.getEventType(),
            CalculationDto.class);
        try {
            var calculationDto = calculationService.increaseValue3(incomingEvent.getPayload().getId());
            incomingEventService.saveWithSuccess(incomingEvent);
            outgoingEventService.createAndSend(incomingEvent, EventTypeEnum.STEP_THREE_END, objectMapper.writeValueAsString(calculationDto), KafkaConfig.SERVICE_TWO_TOPIC);
        } catch (Exception e) {
            log.error(e.getMessage());
            incomingEventService.saveWithError(incomingEvent);
        }
    }
}
