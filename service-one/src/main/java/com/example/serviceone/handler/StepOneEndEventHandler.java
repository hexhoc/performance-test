package com.example.serviceone.handler;

import com.example.serviceone.config.KafkaConfig;
import com.example.serviceone.constant.EventTypeEnum;
import com.example.serviceone.dto.ContainerUpdateResponse;
import com.example.serviceone.entity.OperationStatusEnum;
import com.example.serviceone.message.event.StepOneEndEvent;
import com.example.serviceone.service.OperationService;
import com.example.transactionalbox.model.IncomingEvent;
import com.example.transactionalbox.service.IncomingEventService;
import com.example.transactionalbox.service.OutgoingEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class StepOneEndEventHandler {
    private final IncomingEventService incomingEventService;
    private final OutgoingEventService outgoingEventService;
    private final OperationService operationService;
    private final ObjectMapper objectMapper;

    @EventListener
    @Timed(value = "step.one.end.handler.time")
    public void handle(StepOneEndEvent stepOneEndEvent) {
        log.info("Handle event: STEP 1 END");
        var incomingEvent = createIncomingEvent(stepOneEndEvent);
        try {
            ContainerUpdateResponse containerUpdateResponse = incomingEvent.getPayload();

            OperationStatusEnum operationStatus = Objects.isNull(containerUpdateResponse.errorCode())
                    ? OperationStatusEnum.COMPLETED
                    : OperationStatusEnum.FAILED;

            operationService.updateStatus(containerUpdateResponse.operationId(), operationStatus);

            incomingEventService.saveWithSuccess(incomingEvent);
            outgoingEventService.createEvent(incomingEvent, EventTypeEnum.STEP_TWO.name(), objectMapper.writeValueAsString(incomingEvent.getPayload()), KafkaConfig.SERVICE_ONE_TOPIC);
        } catch (Exception e) {
            log.error(e.getMessage());
            incomingEventService.saveWithError(incomingEvent);
        }
    }

    private IncomingEvent<ContainerUpdateResponse> createIncomingEvent(StepOneEndEvent stepOneEndEvent) {
        return incomingEventService.createEvent(
                stepOneEndEvent.getPayload(),
                stepOneEndEvent.getTraceId(),
                stepOneEndEvent.getRequestId(),
                stepOneEndEvent.getFrom(),
                stepOneEndEvent.getEventType(),
                ContainerUpdateResponse.class);
    }
}
