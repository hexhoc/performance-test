package com.example.serviceone.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

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
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "incoming_event_id", columnDefinition = "uuid", nullable = false)
    private UUID incomingEventId;

    @Column(name = "request_id", columnDefinition = "uuid", nullable = false)
    private UUID requestId;

    @Column(name = "trace_id", columnDefinition = "uuid", nullable = false)
    private UUID traceId;

    @Column(name = "destination", nullable = false, length = 50)
    private String destination;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Type(JsonBinaryType.class)
    @Column(name = "response", nullable = false, columnDefinition = "jsonb")
    private String response; // Assuming the response is a JSON String

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}