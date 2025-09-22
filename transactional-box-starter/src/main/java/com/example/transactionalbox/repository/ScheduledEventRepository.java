package com.example.transactionalbox.repository;

import com.example.transactionalbox.entity.ScheduledEventEntity;
import com.example.transactionalbox.enumeration.MessageBrokerEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ScheduledEventRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ScheduledEventRowMapper rowMapper = new ScheduledEventRowMapper();

    public ScheduledEventEntity save(ScheduledEventEntity entity) {
        String sql = """
            INSERT INTO scheduled_events (
                request_id,
                destination,
                trace_id,
                message_broker,
                body,
                headers,
                created_at
            ) VALUES (
                :requestId,
                :destination,
                :traceId,
                :messageBroker,
                :body::jsonb,
                :headers,
                :createdAt
            )
            RETURNING id
            """;

        Map<String, Object> params = new HashMap<>();
        params.put("requestId", entity.getRequestId());
        params.put("destination", entity.getDestination());
        params.put("traceId", entity.getTraceId());
        params.put("messageBroker", entity.getMessageBroker());
        params.put("body", entity.getPayload());
        params.put("headers", entity.getHeaders());
        params.put("createdAt", entity.getCreatedAt());

        Long generatedId = jdbcTemplate.queryForObject(sql, params, Long.class);
        entity.setSerialNumber(generatedId);

        return entity;
    }

    public List<ScheduledEventEntity> findAllOrderBySerialNumberAsc(Integer limit) {
        // TODO: performance test. Order maybe heavy operation
        String sql = "SELECT * FROM scheduled_events ORDER BY serial_num ASC LIMIT :limit";

        Map<String, Object> params = new HashMap<>();
        params.put("limit", limit);

        try {
            return jdbcTemplate.query(sql, params, rowMapper);
        } catch (Exception e) {
            return List.of();
        }
    }

    @Transactional
    public void deleteAllByIdInBatch(List<Long> serials) {
        if (serials == null || serials.isEmpty()) {
            return;
        }

        String sql = "DELETE FROM scheduled_event_entity WHERE serial_num IN (:serials)";

        Map<String, Object> params = new HashMap<>();
        params.put("serials", serials);

        jdbcTemplate.update(sql, params);
    }

    public List<ScheduledEventEntity> saveAll(List<ScheduledEventEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }

        String sql = """
        INSERT INTO scheduled_event_entity (request_id, destination, trace_id, message_broker, 
                                           payload, headers, created_at)
        VALUES (:requestId, :destination, :traceId, :messageBroker, 
                :payload::jsonb, :headers::jsonb, :createdAt)
        """;

        SqlParameterSource[] batchParams = entities.stream()
                .map(entity -> new MapSqlParameterSource()
                        .addValue("requestId", entity.getRequestId())
                        .addValue("destination", entity.getDestination())
                        .addValue("traceId", entity.getTraceId())
                        .addValue("messageBroker", entity.getMessageBroker())
                        .addValue("payload", entity.getPayload())
                        .addValue("headers", entity.getHeaders())
                        .addValue("createdAt", entity.getCreatedAt()))
                .toArray(SqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(sql, batchParams);

        return entities;
    }

    // RowMapper implementation
    private static class ScheduledEventRowMapper implements RowMapper<ScheduledEventEntity> {
        @Override
        public ScheduledEventEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return ScheduledEventEntity.builder()
                    .serialNumber(rs.getLong("serial_number"))
                    .requestId(rs.getString("request_id"))
                    .destination(rs.getString("destination"))
                    .traceId(rs.getString("trace_id"))
                    .messageBroker(MessageBrokerEnum.valueOf(rs.getString("message_broker")))
                    .payload(rs.getString("body"))
                    .headers(rs.getString("headers"))
                    .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                    .build();
        }
    }
}
