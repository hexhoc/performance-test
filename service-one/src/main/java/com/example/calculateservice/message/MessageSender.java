package com.example.calculateservice.message;


import com.example.calculateservice.entity.OutgoingEventEntity;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

/**
 * Helper to send messages, currently nailed to Kafka, but could also send via AMQP (e.g. RabbitMQ) or
 * any other transport easily
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MessageSender {

  private final KafkaTemplate<String, String> kafkaTemplate;

  public void send(OutgoingEventEntity outgoingEvent, String topicName) {
    try {
      ProducerRecord<String, String> record = new ProducerRecord<String, String>(
          topicName,
          outgoingEvent.getRequestId().toString(),
          outgoingEvent.getPayload());
      record.headers().add("requestId", outgoingEvent.getRequestId().toString().getBytes());
      record.headers().add("traceId", outgoingEvent.getTraceId().toString().getBytes());
      record.headers().add("from", outgoingEvent.getDestination().getBytes());
      record.headers().add("eventType", outgoingEvent.getEventType().getBytes());

      // and send it
      CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(record);
      future.whenComplete(
          (res, error) -> {
            if (error != null) {
                log.error("Unable to deliver message [{}]. {}", res, error.getMessage());
            } else if (res != null) {
                log.info("Sent({}): {}", res.getProducerRecord().key(), res.getProducerRecord().value());
//               log.info("Message [{}] delivered with offset {}", res, res.getRecordMetadata().offset());
            }
          });
    } catch (Exception e) {
      throw new RuntimeException("Could not transform and send message: "+ e.getMessage(), e);
    }
  }
}
