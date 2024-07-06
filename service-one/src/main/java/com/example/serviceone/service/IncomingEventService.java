package com.example.serviceone.service;

import com.example.serviceone.constant.EventStatusEnum;
import com.example.serviceone.constant.EventTypeEnum;
import com.example.serviceone.constant.SourceEnum;
import com.example.serviceone.entity.IncomingEventEntity;
import com.example.serviceone.message.Message;
import com.example.serviceone.repository.IncomingEventRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncomingEventService {
    private final IncomingEventRepository incomingEventRepository;

    public Boolean alreadyExist(UUID correlationId) {
        log.info("Trying to find an existing event");
        return incomingEventRepository.existsById(correlationId);
    }

    public void createEvent(String request, UUID traceId, UUID requestId, String source, String eventType) {
        var incomingEvent = new IncomingEventEntity(
            null,
            traceId,
            requestId,
            EventStatusEnum.SUCCESS,
            source,
            eventType,
            request,
            LocalDateTime.now());

        incomingEventRepository.save(incomingEvent);
    }

    public IncomingEventEntity createEvent(String request, EventTypeEnum eventType, SourceEnum source) {
        var incomingEvent = new IncomingEventEntity(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            EventStatusEnum.SUCCESS,
            source.name(),
            eventType.name(),
            request,
            LocalDateTime.now());

        return incomingEventRepository.save(incomingEvent);
    }
}