package com.example.transactionalbox.manager;

import com.example.transactionalbox.configuration.BlockingOutgoingEventManagerProperties;
import com.example.transactionalbox.enumeration.MessageBrokerEnum;
import com.example.transactionalbox.model.ScheduledEvent;
import com.example.transactionalbox.producer.ProduceOutgoingEventService;
import com.example.transactionalbox.service.ScheduledOutgoingEventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlockingScheduledEventManager implements ScheduledEventManager {

    private final ScheduledOutgoingEventService scheduledOutgoingEventService;
    private final BlockingOutgoingEventManagerProperties managerProperties;
    private final Map<MessageBrokerEnum, ProduceOutgoingEventService<?>> produceEventServices;

    /**
     * load list of scheduled events from db.<br>
     * send them to kafka asynchronously<br>
     * delete loaded list of scheduled events in jpa transaction<br>
     * synchronized is needed to prevent running manager simultaneously from different threads<br>
     */
    @SchedulerLock(
            name = "BlockingScheduledEventManager_sendScheduledEvents",
            lockAtLeastFor = "${transactional-box.blocking-manager.lock-at-least-for:PT1S}",
            lockAtMostFor = "${transactional-box.blocking-manager.lock-at-most-for:PT2M}"
    )
    @Transactional
    public synchronized void sendScheduledEvents() {
        var orderedEvents = scheduledOutgoingEventService.getScheduledEventsOrderedBySerialNumberAsc(managerProperties.getSliceSize());
        if (orderedEvents.isEmpty()) {
            return;
        }
        log.trace("Loaded events to produce count: {}", orderedEvents.size());
        eventsAsMap(orderedEvents).forEach(this::produce);

        scheduledOutgoingEventService.deleteEventsBySerials(serials(orderedEvents));
    }

    private void produce(MessageBrokerEnum messageBroker, List<ScheduledEvent> scheduledEvents) {
        var produceEventService = produceEventServices.get(messageBroker);
        if (produceEventService == null) {
            throw new IllegalStateException("No producer found for message broker " + messageBroker);
        }
        produceEventService.produce(scheduledEvents);
    }

    private Map<MessageBrokerEnum, List<ScheduledEvent>> eventsAsMap(List<ScheduledEvent> events) {
        return events.stream()
                .collect(Collectors.toMap(ScheduledEvent::getMessageBroker,
                        List::of,
                        BlockingScheduledEventManager::concatLists));
    }

    private Map<MessageBrokerEnum, ProduceOutgoingEventService<?>> servicesAsMap(List<ProduceOutgoingEventService<?>> outgoingEventServices) {
        return outgoingEventServices.stream()
                .collect(Collectors.toMap(
                        ProduceOutgoingEventService::getSupportedMessageBroker,
                        Function.identity()));
    }

    private List<Long> serials(List<ScheduledEvent> orderedEvents) {
        return orderedEvents.stream()
                .map(ScheduledEvent::getSerialNumber)
                .toList();
    }

    private static <T> List<T> concatLists(List<T> left, List<T> right) {
        return Stream.concat(left.stream(), right.stream()).toList();
    }
}

