package com.example.serviceone.repository;

import com.example.serviceone.entity.IncomingEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IncomingEventRepository extends JpaRepository<IncomingEventEntity, UUID> {
    Optional<IncomingEventEntity> findByTraceId(String traceId);
    Optional<IncomingEventEntity> findByRequestId(UUID requestId);
}
