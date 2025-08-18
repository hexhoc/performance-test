package com.example.transactionalbox.configuration;

import com.example.transactionalbox.mapper.IncomingEventMapper;
import com.example.transactionalbox.repository.IncomingEventRepository;
import com.example.transactionalbox.repository.OutgoingEventRepository;
import com.example.transactionalbox.service.IncomingEventService;
import com.example.transactionalbox.service.OutgoingEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
@RequiredArgsConstructor
public class TransactionalBoxAutoconfiguration {

    private final ObjectMapper objectMapper;

    @Bean
    @ConditionalOnMissingBean
    public IncomingEventRepository incomingEventRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new IncomingEventRepository(jdbcTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public OutgoingEventRepository outgoingEventRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new OutgoingEventRepository(jdbcTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public IncomingEventService incomingEventService(
            IncomingEventRepository incomingEventRepository,
            IncomingEventMapper incomingEventMapper) {
        return new IncomingEventService(incomingEventRepository, incomingEventMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public OutgoingEventService outgoingEventService(
            OutgoingEventRepository outgoingEventRepository) {
        return new OutgoingEventService(outgoingEventRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public IncomingEventMapper incomingEventMapper() {
        return new IncomingEventMapper(objectMapper);
    }
}
