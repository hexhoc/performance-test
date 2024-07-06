package com.example.calculateservice.mapper;

import com.example.calculateservice.entity.IncomingEventEntity;
import com.example.calculateservice.model.IncomingEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IncomingEventMapper {
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public <T> IncomingEvent<T> toModel(IncomingEventEntity entity, Class<T> payloadType) {
        var model = new IncomingEvent<T>();
        model.setId(entity.getId());
        model.setStatus(entity.getStatus());
        model.setRequestId(entity.getRequestId());
        model.setTraceId(entity.getTraceId());
        model.setSource(entity.getSource());
        if (Objects.nonNull(entity.getPayload())) {
            model.setPayload(objectMapper.readValue(entity.getPayload(), payloadType));
        }
        model.setEventType(entity.getEventType());

        return model;
    }

    @SneakyThrows
    public IncomingEventEntity toEntity(IncomingEvent<?> model) {
        var entity = new IncomingEventEntity();
        entity.setId(model.getId());
        entity.setStatus(model.getStatus());
        entity.setRequestId(model.getRequestId());
        entity.setTraceId(model.getTraceId());
        entity.setSource(model.getSource());
        if (Objects.nonNull(model.getPayload())) {
            entity.setPayload(objectMapper.writeValueAsString(model.getPayload()));
        } else {
            entity.setPayload(null);
        }
        entity.setEventType(model.getEventType());

        return entity;
    }
}
