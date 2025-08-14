package com.example.serviceone.model;

import com.example.serviceone.constant.EventStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class IncomingEvent<T> {
    private UUID id;
    private UUID requestId;
    private String traceId;
    private EventStatusEnum status;
    private String source;
    private String eventType;
    private T payload; // Assuming the request is a JSON String
    private LocalDateTime createdAt;


}
