package com.example.serviceone.handler;

import com.example.serviceone.config.KafkaConfig;
import com.example.serviceone.constant.EventTypeEnum;
import com.example.serviceone.dto.ContainerUpdateRequest;
import com.example.serviceone.utils.TraceUtil;
import com.example.transactionalbox.constant.SourceEnum;
import com.example.transactionalbox.enumeration.MessageBrokerEnum;
import com.example.transactionalbox.model.IncomingEvent;
import com.example.transactionalbox.service.IncomingEventService;
import com.example.transactionalbox.service.OutgoingEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class StepOneCommandHandler {

    private final IncomingEventService incomingEventService;
    private final OutgoingEventService outgoingEventService;
    private final ObjectMapper objectMapper;

    @Timed(value = "step.one.start.handler.time")
    @SneakyThrows
    public void handle(ContainerUpdateRequest containerUpdateRequest) {
        log.info("Handle event: STEP 1 START");
        String payload = objectMapper.writeValueAsString(containerUpdateRequest);
        var incomingEvent = createIncomingEvent(payload);

        try {
            incomingEventService.saveWithSuccess(incomingEvent);
            createOutgoingEvent(incomingEvent, payload);
        } catch (Exception e) {
            incomingEventService.saveWithError(incomingEvent, e);
            throw new RuntimeException(e);
        }
    }

    private IncomingEvent<ContainerUpdateRequest> createIncomingEvent(String payload) {
        return incomingEventService.createEvent(
                payload,
                TraceUtil.getTraceId(),
                SourceEnum.HTTP.name(),
                EventTypeEnum.STEP_ONE.name(),
                ContainerUpdateRequest.class);
    }

    @SneakyThrows
    private void createOutgoingEvent(IncomingEvent<ContainerUpdateRequest> incomingEvent, String payload) {

        var headers = Map.of(
                "requestId", incomingEvent.getRequestId().toString(),
                "traceId", incomingEvent.getTraceId(),
                "from", KafkaConfig.SERVICE_ONE_TOPIC,
                "eventType", EventTypeEnum.STEP_ONE.name()
        );

        var headersString = objectMapper.writeValueAsString(headers);

        outgoingEventService.createEvent(
                incomingEvent,
                headersString,
                EventTypeEnum.STEP_ONE.name(),
                payload,
                KafkaConfig.SERVICE_ONE_TOPIC,
                MessageBrokerEnum.KAFKA);
    }
}
