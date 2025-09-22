package com.example.transactionalbox.producer;

import com.example.transactionalbox.enumeration.MessageBrokerEnum;
import com.example.transactionalbox.model.ScheduledEvent;

import java.util.List;
import java.util.concurrent.Future;


public interface ProduceOutgoingEventService<T> {

    List<Future<T>> produce(List<ScheduledEvent> events);

    MessageBrokerEnum getSupportedMessageBroker();
}
