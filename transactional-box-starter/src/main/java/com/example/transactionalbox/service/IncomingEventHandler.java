package com.example.transactionalbox.service;


import com.example.transactionalbox.model.IncomingEvent;

public interface IncomingEventHandler<T> {

    void handle(IncomingEvent<T> incomingEvent);
    Class<T> getPayloadType();

}
