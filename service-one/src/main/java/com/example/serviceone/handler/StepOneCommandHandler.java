package com.example.serviceone.handler;

import com.example.serviceone.config.KafkaConfig;
import com.example.serviceone.constant.EventTypeEnum;
import com.example.serviceone.constant.SourceEnum;
import com.example.serviceone.service.IncomingEventService;
import com.example.serviceone.service.OperationService;
import com.example.serviceone.service.OutgoingEventService;
import com.example.serviceone.utils.TraceUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class StepOneCommandHandler {

    private final IncomingEventService incomingEventService;
    private final OutgoingEventService outgoingEventService;
    private final OperationService operationService;
    private final ObjectMapper objectMapper;

    @Timed(value = "step.one.start.handler.time")
    public CalculationDto handle(UUID id) {
        log.info("Handle event: STEP 1 START");
        var incomingEvent = incomingEventService.createEvent(
            "{\"id\":%d}".formatted(id),
            EventTypeEnum.STEP_ONE,
            SourceEnum.HTTP,
            TraceUtil.getTraceId(),
            Object.class);
        try {
            var calculationDto = operationService.findById(id)
                .orElseGet(() -> operationService.create(id));
            incomingEventService.saveWithSuccess(incomingEvent);
            outgoingEventService.createAndSend(incomingEvent, EventTypeEnum.STEP_ONE, objectMapper.writeValueAsString(calculationDto), KafkaConfig.SERVICE_ONE_TOPIC);

            return calculationDto;
        } catch (Exception e) {
            incomingEventService.saveWithError(incomingEvent);
            throw new RuntimeException(e);
        }
    }
}
