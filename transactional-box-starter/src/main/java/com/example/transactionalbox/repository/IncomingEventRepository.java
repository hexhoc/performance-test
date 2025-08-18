package com.example.transactionalbox.repository;

import com.example.transactionalbox.constant.EventStatusEnum;
import com.example.transactionalbox.entity.IncomingEventEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class IncomingEventRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final IncomingEventRowMapper rowMapper;

    public IncomingEventRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new IncomingEventRowMapper();
    }

    // 1. Save method
    public IncomingEventEntity save(IncomingEventEntity entity) {
        String sql = """
            INSERT INTO incoming_events (request_id, trace_id, status, source, event_type, payload, created_at)
            VALUES (:requestId, :traceId, :status, :source, :eventType, :payload::jsonb, :createdAt)
            RETURNING id
            """;

        Map<String, Object> params = new HashMap<>();
        params.put("requestId", entity.getRequestId());
        params.put("traceId", entity.getTraceId());
        params.put("status", entity.getStatus() != null ? entity.getStatus().name() : null);
        params.put("source", entity.getSource());
        params.put("eventType", entity.getEventType());
        params.put("payload", entity.getPayload());
        params.put("createdAt", entity.getCreatedAt());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, new MapSqlParameterSource(params), keyHolder, new String[]{"id"});

        if (keyHolder.getKey() != null) {
            UUID generatedId = (UUID) keyHolder.getKeys().get("id");
            entity.setId(generatedId);
        }

        return entity;
    }

    // 2. Find by ID method
    public Optional<IncomingEventEntity> findById(UUID correlationId) {
        String sql = "SELECT * FROM incoming_events WHERE id = :id";

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
        String sql = "SELECT COUNT(*) > 0 FROM incoming_events WHERE id = :id";

        Map<String, Object> params = new HashMap<>();
        params.put("id", correlationId);

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, params, Boolean.class));
    }

    // RowMapper implementation
    private static class IncomingEventRowMapper implements RowMapper<IncomingEventEntity> {
        @Override
        public IncomingEventEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return IncomingEventEntity.builder()
                    .id((UUID) rs.getObject("id"))
                    .requestId((UUID) rs.getObject("request_id"))
                    .traceId(rs.getString("trace_id"))
                    .status(rs.getString("status") != null ?
                            EventStatusEnum.valueOf(rs.getString("status")) : null)
                    .source(rs.getString("source"))
                    .eventType(rs.getString("event_type"))
                    .payload(rs.getString("payload"))
                    .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                    .build();
        }
    }
}