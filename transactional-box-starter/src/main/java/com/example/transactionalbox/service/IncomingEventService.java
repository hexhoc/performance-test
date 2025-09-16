package com.example.transactionalbox.service;

import com.example.transactionalbox.constant.EventStatusEnum;
import com.example.transactionalbox.entity.IncomingEventEntity;
import com.example.transactionalbox.mapper.IncomingEventMapper;
import com.example.transactionalbox.model.IncomingEvent;
import com.example.transactionalbox.repository.IncomingEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

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

    public Optional<IncomingEventEntity> findByRequestIdAndEventTypeOrderByCreatedAtDesc(UUID requestId, String eventType)  {
        return incomingEventRepository.findByRequestIdAndEventTypeOrderByCreatedAtDesc(requestId, eventType);
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
                null,
                request,
                LocalDateTime.now(),
                0);

        return incomingEventMapper.toModel(incomingEventEntity, payloadType);

    }

    @Transactional
    public <T> IncomingEvent<T> createEvent(String request, String traceId, String source, String eventType, Class<T> payloadType) {
        return createEvent(request, traceId, UUID.randomUUID(), source, eventType, payloadType);
    }

    @Transactional
    public void saveWithSuccess(IncomingEvent<?> incomingEvent) {
        incomingEvent.setStatus(EventStatusEnum.SUCCESS);
        var entity = incomingEventMapper.toEntity(incomingEvent);
        incomingEventRepository.save(entity);
    }

    @Transactional
    public void saveWithError(IncomingEvent<?> incomingEvent, Exception e) {
        incomingEvent.setStatus(EventStatusEnum.FAILED);
        incomingEvent.setComment(e.getMessage());
        var entity = incomingEventMapper.toEntity(incomingEvent);
        incomingEventRepository.save(entity);
    }
}