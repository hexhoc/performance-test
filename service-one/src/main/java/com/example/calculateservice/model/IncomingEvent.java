package com.example.calculateservice.model;

import com.example.calculateservice.constant.EventStatusEnum;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncomingEvent<T> {
    private UUID id;
    private UUID requestId;
    private UUID traceId;
    private EventStatusEnum status;
    private String source;
    private String eventType;
    private T payload; // Assuming the request is a JSON String
    private LocalDateTime createdAt;


}
