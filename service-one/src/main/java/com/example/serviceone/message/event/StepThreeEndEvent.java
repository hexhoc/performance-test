package com.example.serviceone.message.event;

import lombok.Getter;

import java.util.UUID;


@Getter
public class StepThreeEndEvent extends BaseEvent {

    /**
     * Construct an instance with the provided source and Kafka event.
     */
    public StepThreeEndEvent(Object source, String payload, UUID requestId, String traceId, String from, String eventType) {
        super(source, payload, requestId, traceId, from, eventType);
    }
}
