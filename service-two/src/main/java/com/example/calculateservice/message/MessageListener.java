package com.example.calculateservice.message;

import com.example.calculateservice.config.KafkaConfig;
import com.example.calculateservice.constant.EventTypeEnum;
import com.example.calculateservice.message.event.StepOneCommand;
import com.example.calculateservice.message.event.StepThreeCommand;
import com.example.calculateservice.message.event.StepTwoCommand;
import com.example.calculateservice.utils.TraceUtil;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
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
        @Payload String messagePayloadJson,
        @Header("requestId") String requestId,
        @Header("traceId") String traceId,
        @Header("from") String from,
        @Header("eventType") String eventType,
        @Header(name = KafkaHeaders.RECEIVED_KEY, required = false) String key,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
//        TraceUtil.updateTraceId(traceId);
        log.info("Received(key={}, partition={}): {}", key, partition, messagePayloadJson);

        if (EventTypeEnum.STEP_ONE.name().equals(eventType)) {
            applicationEventPublisher.publishEvent(new StepOneCommand(this, messagePayloadJson, UUID.fromString(requestId), traceId, from, eventType));
        } else if (EventTypeEnum.STEP_TWO.name().equals(eventType)) {
            applicationEventPublisher.publishEvent(new StepTwoCommand(this, messagePayloadJson, UUID.fromString(requestId), traceId, from, eventType));
        } else if (EventTypeEnum.STEP_THREE.name().equals(eventType)) {
            applicationEventPublisher.publishEvent(new StepThreeCommand(this, messagePayloadJson, UUID.fromString(requestId), traceId, from, eventType));
        } else {
            log.info("Ignored message of type {}", eventType);
        }
    }
}
