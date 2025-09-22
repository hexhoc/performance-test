package com.example.transactionalbox.service;

import com.example.transactionalbox.repository.OutgoingEventRepository;
import com.example.transactionalbox.repository.ScheduledEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class OutgoingEventService {
    private final OutgoingEventRepository outgoingEventRepository;
    private final ScheduledEventRepository scheduledEventRepository;
    // private final MessageSender messageSender;

    public Boolean alreadyExist(UUID correlationId) {
        log.info("Trying to find an existing event");
        return outgoingEventRepository.existsById(correlationId);
    }

}