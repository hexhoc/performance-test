package com.example.transactionalbox.producer;

import com.example.transactionalbox.enumeration.MessageBrokerEnum;
import com.example.transactionalbox.model.ScheduledEvent;
import com.example.transactionalbox.service.ScheduledOutgoingEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@RequiredArgsConstructor
public class KafkaEventProducer implements ProduceOutgoingEventService<SendResult<String, String>> {

    /**
     * Have to remove traceparent header explicitly, because
     * org.springframework.kafka.support.micrometer.KafkaRecordSenderContext adds headers without checks for duplications.
     */
    private static final Set<String> TECHNICAL_HEADERS = Set.of("traceparent");

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final TraceWrapper traceWrapper;
    private final ScheduledOutgoingEventService scheduledOutgoingEventService;

    @Override
    @Timed(
            value = MetricNameConstants.SERVICE_SEND_DURATION,
            extraTags = {
                    MetricTagConstants.TARGET_APPLICATION, MetricTagConstants.KAFKA_APPLICATION_VALUE,
                    MetricTagConstants.PROTOCOL, MetricTagConstants.PROTOCOL_KAFKA_VALUE
            }
    )
    public List<Future<SendResult<String, String>>> produce(List<ScheduledEvent> events) {
        return events.stream()
                .map(this::traceWrapProducing)
                .toList();
    }

    @Override
    public MessageBrokerEnum getSupportedMessageBroker() {
        return MessageBrokerEnum.KAFKA;
    }

    private Future<SendResult<String, String>> traceWrapProducing(ScheduledEvent event) {
        return traceWrapper.withRequestMetadata(
                event.getRequestId(),
                event.getTraceId(),
                () -> produceInternal(event)
        );
    }

    private Future<SendResult<String, String>> produceInternal(ScheduledEvent event) {
        log.debug("Sending an event to the topic: '{}', key: '{}', value: {}", event.getDestination(), event.getRequestId(), event.getBody());
        var producerRecord = getProducerRecord(event);
        prepareHeaders(producerRecord, event.getHeaders());
        var future = kafkaTemplate.send(producerRecord);
        future.whenComplete(futureCallback(event));
        return future;
    }

    private void prepareHeaders(ProducerRecord<String, String> producerRecord, Map<String, Object> headers) {
        if (headers == null) {
            return;
        }
        headers.entrySet()
                .stream()
                .filter(this::notNullValue)
                .filter(this::notTechnical)
                .forEach(entry -> addToHeaders(entry.getKey(), entry.getValue(), producerRecord));
    }

    private void addToHeaders(String key, Object value, ProducerRecord<?, ?> producerRecord) {
        producerRecord.headers()
                .add(key, value.toString().getBytes(UTF_8));
    }

    private boolean notNullValue(Map.Entry<String, Object> entry) {
        return entry.getValue() != null;
    }

    private boolean notTechnical(Map.Entry<String, Object> entry) {
        return !TECHNICAL_HEADERS.contains(entry.getKey());
    }

    private KafkaListenableFutureCallback futureCallback(ScheduledEvent event) {
        return new KafkaListenableFutureCallback(event, MdcContextMapDecorator.ofCurrentMdc(), scheduledOutgoingEventService);
    }

    private ProducerRecord<String, String> getProducerRecord(ScheduledEvent event) {
        return new ProducerRecord<>(
                event.getDestination(),
                null,
                System.currentTimeMillis(),
                event.getRequestId(),
                event.getBody()
        );
    }

}
