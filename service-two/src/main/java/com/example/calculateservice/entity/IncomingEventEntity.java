package com.example.calculateservice.entity;

import com.example.calculateservice.constant.EventStatusEnum;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.hibernate.annotations.Type;

@Entity
@Table(name = "incoming_events")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncomingEventEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "request_id", columnDefinition = "uuid", nullable = false)
    private UUID requestId;

    @Column(name = "trace_id", nullable = false, length = 50)
    private String traceId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EventStatusEnum status;

    @Column(name = "source", nullable = false, length = 50)
    private String source;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Type(JsonBinaryType.class)
    @Column(name = "payload", columnDefinition = "jsonb")
    private String payload; // Assuming the request is a JSON String

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}