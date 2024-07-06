package com.example.serviceone.service;

import com.example.serviceone.config.KafkaConfig;
import com.example.serviceone.constant.EventTypeEnum;
import com.example.serviceone.entity.OutgoingEventEntity;
import com.example.serviceone.message.MessageSender;
import com.example.serviceone.model.IncomingEvent;
import com.example.serviceone.repository.OutgoingEventRepository;
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
    public void createAndSend(IncomingEvent<?> incomingEvent, String response, String topic) {
        var outgoingEventEntity = new OutgoingEventEntity(
            UUID.randomUUID(),
            incomingEvent.getId(),
            incomingEvent.getTraceId(),
            incomingEvent.getRequestId(),
            KafkaConfig.SERVICE_ONE_TOPIC,
            EventTypeEnum.STEP_ONE.name(),
            response,
            LocalDateTime.now());

        outgoingEventRepository.save(outgoingEventEntity);
        messageSender.send(outgoingEventEntity, topic);
    }
}