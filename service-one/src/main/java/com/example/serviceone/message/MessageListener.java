package com.example.serviceone.message;

import static com.example.serviceone.constant.EventTypeEnum.STEP_ONE_END;

import com.example.serviceone.config.KafkaConfig;
import com.example.serviceone.message.event.StepOneEndEvent;
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
        id = "serviceOneConsume",
        topics = {KafkaConfig.SERVICE_TWO_TOPIC},
        groupId = "serviceOneGroup"
    )
    public void orderEventListener(
        String messagePayloadJson,
        @Header("requestId") String requestId,
        @Header("traceId") String traceId,
        @Header("from") String from,
        @Header("eventType") String eventType) {
        log.info(eventType);

        if (STEP_ONE_END.name().equals(eventType)) {
            applicationEventPublisher.publishEvent(new StepOneEndEvent(this, messagePayloadJson, UUID.fromString(requestId), UUID.fromString(traceId), from, eventType));
        } else {
            log.info("Ignored message of type {}", eventType);
        }
    }
}
