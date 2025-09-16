package com.example.transactionalbox.entity;

import com.example.transactionalbox.enumeration.MessageBrokerEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "outgoing_events")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutgoingEventEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "incoming_event_id", columnDefinition = "uuid", nullable = false)
    private UUID incomingEventId;

    @Column(name = "request_id", columnDefinition = "uuid", nullable = false)
    private String requestId;

    @Column(name = "trace_id", nullable = false, length = 50)
    private String traceId;

    @Column(name = "destination", nullable = false, length = 50)
    private String destination;

    @Column(name = "message_broker", nullable = false, length = 50)
    private MessageBrokerEnum messageBroker;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(name = "headers", columnDefinition = "jsonb")
    private String headers;

    @Column(name = "payload", nullable = true, columnDefinition = "jsonb")
    private String payload; // Assuming the response is a JSON String

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}