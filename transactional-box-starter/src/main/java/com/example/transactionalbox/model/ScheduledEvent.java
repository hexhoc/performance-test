package com.example.transactionalbox.model;

import com.example.transactionalbox.enumeration.MessageBrokerEnum;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@Builder
@ToString(exclude = "traceId")
public class ScheduledEvent {
    private Long serialNumber;
    private String requestId;
    private String payload;
    private String destination;
    private String traceId;
    private MessageBrokerEnum messageBroker;
    private Map<String, Object> headers;
}
