package com.example.calculateservice.message.event;

import java.util.UUID;
import lombok.Getter;


@Getter
public class StepTwoCommand extends BaseEvent {

    /**
     * Construct an instance with the provided source and Kafka event.
     */
    public StepTwoCommand(Object source, String payload, UUID requestId, String traceId, String from, String eventType) {
        super(source, payload, requestId, traceId, from, eventType);
    }
}
