package com.example.transactionalbox.mapper;

import com.example.transactionalbox.entity.OutgoingEventEntity;
import com.example.transactionalbox.model.OutgoingEvent;
import org.springframework.stereotype.Component;

@Component
// TODO: Create bean
public class OutgoingEventMapper {

    public OutgoingEventEntity clone(OutgoingEventEntity source) {
        return OutgoingEventEntity.builder()
                .id(source.getId())
                .incomingEventId(source.getIncomingEventId())
                .requestId(source.getRequestId())
                .traceId(source.getTraceId())
                .destination(source.getDestination())
                .messageBroker(source.getMessageBroker())
                .eventType(source.getEventType())
                .headers(source.getHeaders())
                .payload(source.getPayload())
                .createdAt(source.getCreatedAt())
                .build();
    }

    public OutgoingEventEntity toEntity(OutgoingEvent source) {
        return OutgoingEventEntity.builder()
                .id(source.getId())
                .incomingEventId(source.getIncomingEventId())
                .requestId(source.getRequestId())
                .traceId(source.getTraceId())
                .destination(source.getDestination())
                .messageBroker(source.getMessageBroker())
                .eventType(source.getEventType())
                .headers(source.getHeaders())
                .payload(source.getPayload())
                .createdAt(source.getCreatedAt())
                .build();
    }
}
