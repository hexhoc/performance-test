package com.example.transactionalbox.mapper;

import com.example.transactionalbox.entity.OutgoingEventEntity;
import com.example.transactionalbox.entity.ScheduledEventEntity;
import com.example.transactionalbox.model.OutgoingEvent;
import com.example.transactionalbox.model.ScheduledEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
// TODO: Create bean
public class ScheduledEventMapper {
    private final ObjectMapper objectMapper;

    public ScheduledEventEntity toEntity(ScheduledEvent source) {
        return ScheduledEventEntity.builder()
                .requestId(source.getRequestId())
                .destination(source.getDestination())
                .traceId(source.getTraceId())
                .messageBroker(source.getMessageBroker())
                .payload(source.getBody())
                .headers(asJson(source.getHeaders()))
//                .createdAt(source.getCreatedAt())
                .build();
    }

    public ScheduledEventEntity toEntity(OutgoingEvent source) {
        return ScheduledEventEntity.builder()
                .requestId(source.getRequestId())
                .destination(source.getDestination())
                .traceId(source.getTraceId())
                .messageBroker(source.getMessageBroker())
                .payload(source.getPayload())
                .headers(asJson(source.getHeaders()))
//                .createdAt(source.getCreatedAt())
                .build();
    }

    public ScheduledEventEntity toEntity(OutgoingEventEntity source, String traceId) {
        return ScheduledEventEntity.builder()
                .requestId(source.getRequestId())
                .destination(source.getDestination())
                .traceId(traceId)
                .messageBroker(source.getMessageBroker())
                .payload(source.getPayload())
                .headers(source.getHeaders())
//                .createdAt(source.getCreatedAt())
                .build();
    }

    @SneakyThrows
    private String asJson(Object source) {
        if (source == null) {
            return null;
        }
        return objectMapper.writeValueAsString(source);
    }

}
