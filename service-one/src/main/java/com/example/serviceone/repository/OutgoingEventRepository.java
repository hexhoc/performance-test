package com.example.serviceone.repository;

import com.example.serviceone.entity.OutgoingEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OutgoingEventRepository extends JpaRepository<OutgoingEventEntity, UUID> {
    Optional<OutgoingEventEntity> findByTraceId(String traceId);
    Optional<OutgoingEventEntity> findByRequestId(UUID correlationId);
}
