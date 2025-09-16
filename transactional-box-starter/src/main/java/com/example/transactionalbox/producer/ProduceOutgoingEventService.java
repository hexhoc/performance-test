package com.example.transactionalbox.producer;

import com.example.transactionalbox.entity.ScheduledEventEntity;
import com.example.transactionalbox.enumeration.MessageBrokerEnum;

import java.util.List;
import java.util.concurrent.Future;


public interface ProduceOutgoingEventService<T> {

    List<Future<T>> produce(List<ScheduledEventEntity> events);

    MessageBrokerEnum getSupportedMessageBroker();
}
