package com.example.calculateservice.message.event;

import java.util.UUID;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public abstract class BaseEvent extends ApplicationEvent {
    private final UUID requestId;
    private final String traceId;
    private final String from;
    private final String eventType;
    private final String payload;

    /**
     * Construct an instance with the provided source and Kafka event.
     *
     * @param source  the container instance that generated the event
     * @param payload event
     */
    public BaseEvent(Object source, String payload, UUID requestId, String traceId, String from, String eventType) {
        super(source);
        this.payload = payload;
        this.requestId = requestId;
        this.traceId = traceId;
        this.from = from;
        this.eventType = eventType;
    }
}
