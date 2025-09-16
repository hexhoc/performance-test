package com.example.transactionalbox.model;

import com.example.transactionalbox.enumeration.MessageBrokerEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class OutgoingEvent {
    private UUID id;
    private UUID incomingEventId;
    private String requestId;
    private String traceId;
    private String destination;
    private MessageBrokerEnum messageBroker;
    private String eventType;
    private String headers; // Assuming the request is a JSON String
    private String payload; // Assuming the request is a JSON String
    private LocalDateTime createdAt;
}