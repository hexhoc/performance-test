package com.example.calculateservice.message.event;

import java.util.UUID;
import lombok.Getter;


@Getter
public class StepThreeCommand extends BaseEvent {

    /**
     * Construct an instance with the provided source and Kafka event.
     */
    public StepThreeCommand(Object source, String payload, UUID requestId, UUID traceId, String from, String eventType) {
        super(source, payload, requestId, traceId, from, eventType);
    }
}
