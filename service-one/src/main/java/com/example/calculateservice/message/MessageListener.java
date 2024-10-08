package com.example.calculateservice.message;

import static com.example.calculateservice.constant.EventTypeEnum.STEP_ONE_END;
import static com.example.calculateservice.constant.EventTypeEnum.STEP_THREE_END;
import static com.example.calculateservice.constant.EventTypeEnum.STEP_TWO_END;

import com.example.calculateservice.config.KafkaConfig;
import com.example.calculateservice.message.event.StepOneEndEvent;
import com.example.calculateservice.message.event.StepThreeEndEvent;
import com.example.calculateservice.message.event.StepTwoEndEvent;
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
        id = "serviceOneConsume",
        topics = {KafkaConfig.SERVICE_TWO_TOPIC},
        groupId = "serviceOneGroup"
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

        if (STEP_ONE_END.name().equals(eventType)) {
            applicationEventPublisher.publishEvent(new StepOneEndEvent(this, messagePayloadJson, UUID.fromString(requestId), traceId, from, eventType));
        } else if (STEP_TWO_END.name().equals(eventType)) {
            applicationEventPublisher.publishEvent(new StepTwoEndEvent(this, messagePayloadJson, UUID.fromString(requestId), traceId, from, eventType));
        } else if (STEP_THREE_END.name().equals(eventType)) {
            applicationEventPublisher.publishEvent(new StepThreeEndEvent(this, messagePayloadJson, UUID.fromString(requestId), traceId, from, eventType));
        } else {
            log.info("Ignored message of type {}", eventType);
        }
    }
}
