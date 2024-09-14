package com.example.calculateservice.service;

import com.example.calculateservice.constant.EventStatusEnum;
import com.example.calculateservice.constant.EventTypeEnum;
import com.example.calculateservice.constant.SourceEnum;
import com.example.calculateservice.entity.IncomingEventEntity;
import com.example.calculateservice.mapper.IncomingEventMapper;
import com.example.calculateservice.model.IncomingEvent;
import com.example.calculateservice.repository.IncomingEventRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncomingEventService {
    private final IncomingEventRepository incomingEventRepository;
    private final IncomingEventMapper incomingEventMapper;

    public Boolean alreadyExist(UUID correlationId) {
        log.info("Trying to find an existing event");
        return incomingEventRepository.existsById(correlationId);
    }

    @Transactional
    public <T> IncomingEvent<T> createEvent(String request, String traceId, UUID requestId, String source, String eventType, Class<T> payloadType) {
        var incomingEventEntity = new IncomingEventEntity(
            UUID.randomUUID(),
            requestId,
            traceId,
            EventStatusEnum.SUCCESS,
            source,
            eventType,
            request,
            LocalDateTime.now());

        return incomingEventMapper.toModel(incomingEventEntity, payloadType);

    }

    @Transactional
    public <T> IncomingEvent<T> createEvent(String request, EventTypeEnum eventType, SourceEnum source, String traceId, Class<T> payloadType) {
        var incomingEventEntity = new IncomingEventEntity(
            UUID.randomUUID(),
            UUID.randomUUID(),
            traceId,
            EventStatusEnum.SUCCESS,
            source.name(),
            eventType.name(),
            request,
            LocalDateTime.now());

        return incomingEventMapper.toModel(incomingEventEntity, payloadType);
    }

    @Transactional
    public void saveWithSuccess(IncomingEvent<?> incomingEvent) {
        incomingEvent.setStatus(EventStatusEnum.SUCCESS);
        var entity = incomingEventMapper.toEntity(incomingEvent);
        incomingEventRepository.save(entity);
    }

    @Transactional
    public void saveWithError(IncomingEvent<?> incomingEvent) {
        incomingEvent.setStatus(EventStatusEnum.FAILED);
        var entity = incomingEventMapper.toEntity(incomingEvent);
        incomingEventRepository.save(entity);
    }
}