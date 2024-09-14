package com.example.calculateservice.repository;

import com.example.calculateservice.entity.IncomingEventEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomingEventRepository extends JpaRepository<IncomingEventEntity, UUID> {
    Optional<IncomingEventEntity> findByTraceId(String traceId);
    Optional<IncomingEventEntity> findByRequestId(UUID requestId);
}
