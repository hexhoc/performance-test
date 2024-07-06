package com.example.calculateservice.message;

import com.example.calculateservice.config.KafkaConfig;
import com.example.calculateservice.constant.EventTypeEnum;
import com.example.calculateservice.message.event.StepOneCommand;
import com.example.calculateservice.message.event.StepThreeCommand;
import com.example.calculateservice.message.event.StepTwoCommand;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageListener {
    private final ApplicationEventPublisher applicationEventPublisher;

    @KafkaListener(
        id = "serviceTwoConsume",
        topics = {KafkaConfig.SERVICE_ONE_TOPIC},
        groupId = "serviceTwoGroup"
    )
    public void orderEventListener(
        String messagePayloadJson,
        @Header("requestId") String requestId,
        @Header("traceId") String traceId,
        @Header("from") String from,
        @Header("eventType") String eventType) {
        log.info(eventType);

        if (EventTypeEnum.STEP_ONE.name().equals(eventType)) {
            applicationEventPublisher.publishEvent(new StepOneCommand(this, messagePayloadJson, UUID.fromString(requestId), UUID.fromString(traceId), from, eventType));
        } else if (EventTypeEnum.STEP_TWO.name().equals(eventType)) {
            applicationEventPublisher.publishEvent(new StepTwoCommand(this, messagePayloadJson, UUID.fromString(requestId), UUID.fromString(traceId), from, eventType));
        } else if (EventTypeEnum.STEP_THREE.name().equals(eventType)) {
            applicationEventPublisher.publishEvent(new StepThreeCommand(this, messagePayloadJson, UUID.fromString(requestId), UUID.fromString(traceId), from, eventType));
        } else {
            log.info("Ignored message of type {}", eventType);
        }
    }
}
