package com.example.serviceone.service;

import com.example.serviceone.constant.EventStatusEnum;
import com.example.serviceone.constant.EventTypeEnum;
import com.example.serviceone.constant.SourceEnum;
import com.example.serviceone.entity.IncomingEventEntity;
import com.example.serviceone.mapper.IncomingEventMapper;
import com.example.serviceone.model.IncomingEvent;
import com.example.serviceone.repository.IncomingEventRepository;
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
    public <T> IncomingEvent<T> createEvent(String request, UUID traceId, UUID requestId, String source, String eventType, Class<T> payloadType) {
        var incomingEventEntity = new IncomingEventEntity(
            null,
            traceId,
            requestId,
            EventStatusEnum.SUCCESS,
            source,
            eventType,
            request,
            LocalDateTime.now());

        return incomingEventMapper.toModel(incomingEventEntity, payloadType);

    }

    @Transactional
    public <T> IncomingEvent<T> createEvent(String request, EventTypeEnum eventType, SourceEnum source, Class<T> payloadType) {
        var incomingEventEntity = new IncomingEventEntity(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID(),
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