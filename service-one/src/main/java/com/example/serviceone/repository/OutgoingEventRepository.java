package com.example.serviceone.repository;

import com.example.serviceone.entity.OutgoingEventEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutgoingEventRepository extends JpaRepository<OutgoingEventEntity, UUID> {
    Optional<OutgoingEventEntity> findByTraceId(UUID traceId);
    Optional<OutgoingEventEntity> findByRequestId(UUID correlationId);
}
