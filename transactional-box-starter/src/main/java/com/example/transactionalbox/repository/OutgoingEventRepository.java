package com.example.transactionalbox.repository;

import com.example.transactionalbox.entity.OutgoingEventEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OutgoingEventRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final OutgoingEventRowMapper rowMapper;

    public OutgoingEventRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new OutgoingEventRowMapper();
    }

    // 1. Save method
    public OutgoingEventEntity save(OutgoingEventEntity entity) {
        String sql = """
            INSERT INTO outgoing_events (
                incoming_event_id,
                request_id,
                trace_id,
                destination,
                event_type,
                payload,
                created_at
            ) VALUES (
                :incomingEventId,
                :requestId,
                :traceId,
                :destination,
                :eventType,
                :payload::jsonb,
                :createdAt
            )
            RETURNING id
            """;

        Map<String, Object> params = new HashMap<>();
        params.put("incomingEventId", entity.getIncomingEventId());
        params.put("requestId", entity.getRequestId());
        params.put("traceId", entity.getTraceId());
        params.put("destination", entity.getDestination());
        params.put("eventType", entity.getEventType());
        params.put("payload", entity.getPayload());
        params.put("createdAt", entity.getCreatedAt());

        UUID generatedId = jdbcTemplate.queryForObject(sql, params, UUID.class);
        entity.setId(generatedId);

        return entity;
    }

    // 2. Find by ID method
    public Optional<OutgoingEventEntity> findById(UUID correlationId) {
        String sql = "SELECT * FROM outgoing_events WHERE id = :id";

        Map<String, Object> params = new HashMap<>();
        params.put("id", correlationId);

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, params, rowMapper));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // 3. Exists by ID method
    public boolean existsById(UUID correlationId) {
        String sql = "SELECT COUNT(*) > 0 FROM outgoing_events WHERE id = :id";

        Map<String, Object> params = new HashMap<>();
        params.put("id", correlationId);

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, params, Boolean.class));
    }

    // RowMapper implementation
    private static class OutgoingEventRowMapper implements RowMapper<OutgoingEventEntity> {
        @Override
        public OutgoingEventEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return OutgoingEventEntity.builder()
                    .id((UUID) rs.getObject("id"))
                    .incomingEventId((UUID) rs.getObject("incoming_event_id"))
                    .requestId((UUID) rs.getObject("request_id"))
                    .traceId(rs.getString("trace_id"))
                    .destination(rs.getString("destination"))
                    .eventType(rs.getString("event_type"))
                    .payload(rs.getString("payload"))
                    .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                    .build();
        }
    }
}