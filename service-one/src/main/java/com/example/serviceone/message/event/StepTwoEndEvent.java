package com.example.serviceone.message.event;

import lombok.Getter;

import java.util.UUID;


@Getter
public class StepTwoEndEvent extends BaseEvent {

    /**
     * Construct an instance with the provided source and Kafka event.
     */
    public StepTwoEndEvent(Object source, String payload, UUID requestId, String traceId, String from, String eventType) {
        super(source, payload, requestId, traceId, from, eventType);
    }
}
