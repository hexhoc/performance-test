package com.example.transactionalbox.service;

import com.example.transactionalbox.entity.OutgoingEventEntity;
import com.example.transactionalbox.model.IncomingEvent;
import com.example.transactionalbox.repository.OutgoingEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutgoingEventService {
    private final OutgoingEventRepository outgoingEventRepository;
    // private final MessageSender messageSender;

    public Boolean alreadyExist(UUID correlationId) {
        log.info("Trying to find an existing event");
        return outgoingEventRepository.existsById(correlationId);
    }

    @Transactional
    public void createAndSend(IncomingEvent<?> incomingEvent, String eventType, String response, String topic) {
        var outgoingEventEntity = new OutgoingEventEntity(
            UUID.randomUUID(),
            incomingEvent.getId(),
            incomingEvent.getRequestId(),
            incomingEvent.getTraceId(),
            topic,
            eventType,
            response,
            LocalDateTime.now());

        outgoingEventRepository.save(outgoingEventEntity);
        // TODO: Send message using functional interface of a host app
        // messageSender.send(outgoingEventEntity, topic);
    }
}