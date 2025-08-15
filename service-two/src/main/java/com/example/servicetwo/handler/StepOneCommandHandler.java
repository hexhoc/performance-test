package com.example.servicetwo.handler;

import com.example.servicetwo.config.KafkaConfig;
import com.example.servicetwo.constant.EventTypeEnum;
import com.example.servicetwo.dto.ContainerUpdateRequest;
import com.example.servicetwo.dto.ContainerUpdateResponse;
import com.example.servicetwo.exception.ErrorCodeValue;
import com.example.servicetwo.message.event.StepOneCommand;
import com.example.servicetwo.model.IncomingEvent;
import com.example.servicetwo.service.ContainerService;
import com.example.servicetwo.service.IncomingEventService;
import com.example.servicetwo.service.OutgoingEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StepOneCommandHandler {
    private final IncomingEventService incomingEventService;
    private final OutgoingEventService outgoingEventService;
    private final ContainerService containerService;
    private final ObjectMapper objectMapper;

    @EventListener
    @SneakyThrows
    public void handle(StepOneCommand stepOneCommand) {
        log.info("Handle event: STEP 1 START");
        var incomingEvent = buildIncomingEvent(stepOneCommand);
            var containerUpdateRequest = incomingEvent.getPayload();

        try {
            containerService.update(containerUpdateRequest);

            String payload = buildContainerUpdateResponsePayload(containerUpdateRequest);

            incomingEventService.saveWithSuccess(incomingEvent);
            outgoingEventService.createAndSend(incomingEvent, EventTypeEnum.STEP_ONE_END, payload, KafkaConfig.SERVICE_TWO_TOPIC);
        } catch (Exception e) {
            log.error(e.getMessage());
            String payload = buildContainerUpdateResponsePayload(containerUpdateRequest, ErrorCodeValue.BUSINESS_ERROR, e.getMessage());
            incomingEventService.saveWithError(incomingEvent);
            outgoingEventService.createAndSend(incomingEvent, EventTypeEnum.STEP_ONE_END, payload, KafkaConfig.SERVICE_TWO_TOPIC);
        }
    }

    @SneakyThrows
    private String buildContainerUpdateResponsePayload(ContainerUpdateRequest containerUpdateRequest) {

        return objectMapper.writeValueAsString(ContainerUpdateResponse.builder()
                .operationId(containerUpdateRequest.operationId())
                .containerId(containerUpdateRequest.id())
                .build());
    }

    @SneakyThrows
    private String buildContainerUpdateResponsePayload(ContainerUpdateRequest containerUpdateRequest, Integer errorCode, String errorMessage) {

        return objectMapper.writeValueAsString(ContainerUpdateResponse.builder()
                .operationId(containerUpdateRequest.operationId())
                .containerId(containerUpdateRequest.id())
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build());

    }

    private IncomingEvent<ContainerUpdateRequest> buildIncomingEvent(StepOneCommand stepOneCommand) {
        return incomingEventService.createEvent(
                stepOneCommand.getPayload(),
                stepOneCommand.getTraceId(),
                stepOneCommand.getRequestId(),
                stepOneCommand.getFrom(),
                stepOneCommand.getEventType(),
                ContainerUpdateRequest.class);
    }
}
