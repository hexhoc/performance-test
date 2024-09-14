package com.example.calculateservice.repository;

import com.example.calculateservice.entity.OutgoingEventEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutgoingEventRepository extends JpaRepository<OutgoingEventEntity, UUID> {
    Optional<OutgoingEventEntity> findByTraceId(String traceId);
    Optional<OutgoingEventEntity> findByRequestId(UUID correlationId);
}
