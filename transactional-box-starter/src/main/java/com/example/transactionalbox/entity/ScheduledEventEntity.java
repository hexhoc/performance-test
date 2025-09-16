package com.example.transactionalbox.entity;

import com.example.transactionalbox.enumeration.MessageBrokerEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduledEventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ap_scheduled_outgoing_event_sequence_generator")
    @SequenceGenerator(name = "ap_scheduled_outgoing_event_sequence_generator", sequenceName = "hibernate_sequence", allocationSize = 1)
    @Column(name = "serial_num", nullable = false)
    private Long serialNumber;

    @Column(name = "request_id", nullable = false)
    private String requestId;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Column(name = "trace_id")
    private String traceId;

    @Column(name = "message_broker", nullable = false)
    private MessageBrokerEnum messageBroker;

    @Column(name = "payload", columnDefinition = "jsonb")
    private String payload;

    @Column(name = "headers", columnDefinition = "jsonb")
    private String headers;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
