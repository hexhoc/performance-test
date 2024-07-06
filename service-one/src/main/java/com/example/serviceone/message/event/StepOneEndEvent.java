package com.example.serviceone.message.event;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEvent;


@Getter
public class StepOneEndEvent extends BaseEvent {

    /**
     * Construct an instance with the provided source and Kafka event.
     */
    public StepOneEndEvent(Object source, String payload, UUID requestId, UUID traceId, String from, String eventType) {
        super(source, payload, requestId, traceId, from, eventType);
    }
}
