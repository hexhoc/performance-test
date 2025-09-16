package com.example.transactionalbox.service;

import com.example.transactionalbox.entity.OutgoingEventEntity;
import com.example.transactionalbox.entity.ScheduledEventEntity;
import com.example.transactionalbox.mapper.OutgoingEventMapper;
import com.example.transactionalbox.mapper.ScheduledEventMapper;
import com.example.transactionalbox.model.IncomingEvent;
import com.example.transactionalbox.model.OutgoingEvent;
import com.example.transactionalbox.repository.OutgoingEventRepository;
import com.example.transactionalbox.repository.ScheduledEventRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
// TODO: Create bean
public class ScheduledOutgoingEventService {
    private final ScheduledEventRepository scheduledEventRepository;
    private final OutgoingEventRepository outgoingEventRepository;
    private final ScheduledEventMapper scheduledEventMapper;
    private final OutgoingEventMapper outgoingEventMapper;
    private final ThreadLocal<IncomingEvent<?>> incomingEventThreadLocal = new ThreadLocal<>();

    public void setIncomingEvent(@NonNull IncomingEvent<?> value) {
        incomingEventThreadLocal.set(value);
    }

    public void removeIncomingEvent() {
        incomingEventThreadLocal.remove();
    }

    public List<ScheduledEventEntity> getScheduledEventsOrderedBySerialNumberAsc(@NonNull Integer sliceSize) {
        return scheduledEventRepository.findAllOrderBySerialNumberAsc(sliceSize);
    }

    @Transactional
    public void deleteEventsBySerials(List<Long> serials) {
        if (CollectionUtils.isEmpty(serials)) {
            return;
        }
        scheduledEventRepository.deleteAllByIdInBatch(serials);
    }

    @Transactional
    public void scheduleEvent(OutgoingEvent outgoingEvent) {
        outgoingEventRepository.save(outgoingEventMapper.toEntity(outgoingEvent));
        scheduledEventRepository.save(scheduledEventMapper.toEntity(outgoingEvent));
    }

    @Transactional
    public void scheduleEvents(@NonNull List<OutgoingEvent> outgoingEvents) {
        doScheduleEvents(outgoingEvents);
    }

    @Transactional
    public <T> void reScheduleEventsByIncomingEvent(@NonNull UUID existingIncomingEventId, @NonNull IncomingEvent<T> receivedEvent) {
        var outgoingEventEntityOpt = outgoingEventRepository.findByIncomingEventId(existingIncomingEventId);
        if (outgoingEventEntityOpt.isPresent()) {
            doScheduleEvent(outgoingEventEntityOpt.get(), receivedEvent);
        } else {
            String message = "Outgoing event not found by incoming event id: %s".formatted(existingIncomingEventId);
            log.error(message);
            throw new RuntimeException(message);
        }
    }

    private void doScheduleEvents(List<OutgoingEvent> outgoingEvents) {
        outgoingEventRepository.saveAll(toOutgoingEntities(outgoingEvents));
        scheduledEventRepository.saveAll(toScheduledEntities(outgoingEvents));
    }

    private <T> void doScheduleEvent(OutgoingEventEntity outgoingEventEntity, IncomingEvent<T> receivedEvent) {
        outgoingEventRepository.save(cloneOutgoingEntities(outgoingEventEntity, receivedEvent));
        scheduledEventRepository.save(scheduledEventMapper.toEntity(outgoingEventEntity, receivedEvent.getTraceId()));
    }

    private OutgoingEventEntity cloneOutgoingEntities(OutgoingEventEntity outgoingEventEntity, IncomingEvent<?> receivedEvent) {
        OutgoingEventEntity clonedOutgoingEventEntity = outgoingEventMapper.clone(outgoingEventEntity);
        updateOutgoingEvent(clonedOutgoingEventEntity, receivedEvent);
        return clonedOutgoingEventEntity;
    }

    private void updateOutgoingEvent(OutgoingEventEntity outgoingEvent, IncomingEvent<?> receivedEvent) {
        outgoingEvent.setId(UUID.randomUUID());
        outgoingEvent.setIncomingEventId(receivedEvent.getId());
    }

    private void validateThreadLocalIncomingEvent() {
        if (incomingEventThreadLocal.get() != null) {
            return;
        }
        throw new IllegalStateException("No incoming event is set to ThreadLocal variable");
    }

    private List<OutgoingEventEntity> toOutgoingEntities(List<OutgoingEvent> outgoingEvents) {
        return outgoingEvents.stream()
                .map(outgoingEventMapper::toEntity)
                .toList();
    }

    private List<ScheduledEventEntity> toScheduledEntities(List<OutgoingEvent> outgoingEvents) {
        return outgoingEvents.stream()
                .map(scheduledEventMapper::toEntity)
                .toList();
    }

}
