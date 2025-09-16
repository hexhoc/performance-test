package com.example.transactionalbox.service;

import com.example.transactionalbox.constant.EventStatusEnum;
import com.example.transactionalbox.entity.IncomingEventEntity;
import com.example.transactionalbox.enumeration.IdempotencyLevelEnum;
import com.example.transactionalbox.model.IdempotencyInfo;
import com.example.transactionalbox.model.IncomingEvent;
import com.example.transactionalbox.utils.TraceUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IncomingEventProcessor {

    private final IncomingEventService incomingEventService;
    private final OutgoingEventService outgoingEventService;
    private final ScheduledOutgoingEventService scheduledOutgoingEventService;

    /**
     * Process incoming messages
     */
    public <T> void process(@NonNull String request, UUID requestId, String source, String eventType, @NonNull IncomingEventHandler<T> incomingEventHandler) {
        String traceId = TraceUtil.getTraceId();

        IncomingEvent<T> incomingEvent = incomingEventService.createEvent(request, traceId, requestId, source, eventType, incomingEventHandler.getPayloadType());
        IdempotencyInfo idempotencyInfo = getIdempotencyLevel(incomingEvent);
        try {
            switch(idempotencyInfo.level()) {
                case NEW, RERUN -> {
                    doProcess(incomingEvent, incomingEventHandler);
                }
                case RESENT -> {
                    doResent(incomingEvent, idempotencyInfo);
                }
                default -> {
                    return;
                }
            }
            incomingEventService.saveWithSuccess(incomingEvent);
        } catch(Exception e) {
            incomingEventService.saveWithError(incomingEvent, e);
        }
    }

    private <T> void doProcess(IncomingEvent<T> incomingEvent, IncomingEventHandler<T> incomingEventHandler) {
        incomingEventHandler.handle(incomingEvent);
    }

    private <T> void doResent(IncomingEvent<T> incomingEvent, IdempotencyInfo idempotencyInfo) {
        // Create new incoming event
        // Create new outgoing event cloning an existing
        // Schedule outgoing event
        scheduledOutgoingEventService.reScheduleEventsByIncomingEvent(idempotencyInfo.existingIncomingEventId(), incomingEvent);
    }

    private <T> IdempotencyInfo getIdempotencyLevel(IncomingEvent<T> incomingEvent) {

        Optional<IncomingEventEntity> existingIncomingEventOpt = incomingEventService.findByRequestIdAndEventTypeOrderByCreatedAtDesc(
                incomingEvent.getRequestId(),
                incomingEvent.getEventType()
        );

        if (existingIncomingEventOpt.isPresent()) {
            IncomingEventEntity existingIncomingEvent = existingIncomingEventOpt.get();
            IdempotencyLevelEnum idempotencyLevel = IdempotencyLevelEnum.RESENT;
            if (existingIncomingEvent.getStatus().equals(EventStatusEnum.FAILED)) {
                idempotencyLevel = IdempotencyLevelEnum.RERUN;
            }
            return new IdempotencyInfo(existingIncomingEvent.getId(), existingIncomingEvent.getVersion() + 1, idempotencyLevel);
        }

        return new IdempotencyInfo(null, 0, IdempotencyLevelEnum.NEW);

    }
}
