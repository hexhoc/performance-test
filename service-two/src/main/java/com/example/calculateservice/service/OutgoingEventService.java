package com.example.calculateservice.service;

import com.example.calculateservice.constant.EventTypeEnum;
import com.example.calculateservice.entity.OutgoingEventEntity;
import com.example.calculateservice.message.MessageSender;
import com.example.calculateservice.model.IncomingEvent;
import com.example.calculateservice.repository.OutgoingEventRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutgoingEventService {
    private final OutgoingEventRepository outgoingEventRepository;
    private final MessageSender messageSender;

    public Boolean alreadyExist(UUID correlationId) {
        log.info("Trying to find an existing event");
        return outgoingEventRepository.existsById(correlationId);
    }

    @Transactional
    public void createAndSend(IncomingEvent<?> incomingEvent, EventTypeEnum eventType, String response, String topic) {
        var outgoingEventEntity = new OutgoingEventEntity(
            UUID.randomUUID(),
            incomingEvent.getId(),
            incomingEvent.getRequestId(),
            incomingEvent.getTraceId(),
            topic,
            eventType.name(),
            response,
            LocalDateTime.now());

        outgoingEventRepository.save(outgoingEventEntity);
        messageSender.send(outgoingEventEntity, topic);
    }
}