package com.example.transactionalbox.configuration;


import com.example.transactionalbox.model.ScheduledEvent;
import com.example.transactionalbox.service.MdcContextMapDecorator;
import com.example.transactionalbox.service.ScheduledOutgoingEventService;
import com.example.transactionalbox.utils.FormatUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.support.SendResult;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Slf4j
@Getter
@RequiredArgsConstructor
public class KafkaListenableFutureCallback implements BiConsumer<SendResult<String, String>, Throwable> {
    private final ScheduledEvent scheduledEvent;
    private final MdcContextMapDecorator mdcContextMapDecorator;
    private final ScheduledOutgoingEventService scheduledOutgoingEventService;

    @Override
    public void accept(SendResult<String, String> result, Throwable throwable) {
        if (throwable != null) {
            onFailure(throwable);
            return;
        }
        onSuccess(result);
    }

    private void onFailure(@NonNull Throwable throwable) {
        mdcContextMapDecorator.decorate(
                () -> {
                    log.error("Cannot send a event, event will be rescheduled: {}", scheduledEvent, throwable);
                    scheduledOutgoingEventService.scheduleEvent(scheduledEvent);
                }
        );
    }

    private void onSuccess(SendResult<String, String> result) {
        mdcContextMapDecorator.decorate(
                () -> log.debug("Event was sent: {}", toString(result))
        );
    }

    private String toString(SendResult<String, String> result) {
        var record = result.getProducerRecord();
        return String.format(
                "topic: '%s',  key: '%s', headers: [%s], value->'%s'",
                record.topic(),
                record.key(),
                headers(record.headers()),
                record.value()
        );
    }

    private String headers(Headers headers) {
        if (headers == null) {
            return null;
        }
        return FormatUtil.format(
                Arrays.stream(headers.toArray())
                        .collect(
                                Collectors.toMap(
                                        Header::key,
                                        header -> new String(header.value(), StandardCharsets.UTF_8)
                                )
                        )
        );
    }
}
