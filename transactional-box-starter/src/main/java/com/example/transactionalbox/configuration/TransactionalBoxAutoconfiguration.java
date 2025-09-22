package com.example.transactionalbox.configuration;

import com.example.transactionalbox.enumeration.MessageBrokerEnum;
import com.example.transactionalbox.manager.BlockingScheduledEventManager;
import com.example.transactionalbox.manager.ScheduledEventManager;
import com.example.transactionalbox.mapper.IncomingEventMapper;
import com.example.transactionalbox.mapper.OutgoingEventMapper;
import com.example.transactionalbox.mapper.ScheduledEventMapper;
import com.example.transactionalbox.producer.ProduceOutgoingEventService;
import com.example.transactionalbox.repository.IncomingEventRepository;
import com.example.transactionalbox.repository.OutgoingEventRepository;
import com.example.transactionalbox.repository.ScheduledEventRepository;
import com.example.transactionalbox.scheduler.ProcessScheduledEventScheduler;
import com.example.transactionalbox.service.IncomingEventService;
import com.example.transactionalbox.service.OutgoingEventService;
import com.example.transactionalbox.service.ScheduledOutgoingEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;

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
    public ScheduledEventRepository scheduledEventRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new ScheduledEventRepository(jdbcTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public IncomingEventMapper incomingEventMapper() {
        return new IncomingEventMapper(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public OutgoingEventMapper outgoingEventMapper() {
        return new OutgoingEventMapper();
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
    public OutgoingEventService outgoingEventService(OutgoingEventRepository outgoingEventRepository,
                                                     ScheduledEventRepository scheduledEventRepository) {
        return new OutgoingEventService(outgoingEventRepository, scheduledEventRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public ScheduledEventMapper scheduledEventMapper() {
        return new ScheduledEventMapper(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public ScheduledOutgoingEventService scheduledOutgoingEventService(ScheduledEventRepository scheduledEventRepository,
                                                                       OutgoingEventRepository outgoingEventRepository,
                                                                       ScheduledEventMapper scheduledEventMapper,
                                                                       OutgoingEventMapper outgoingEventMapper) {
        return new ScheduledOutgoingEventService(scheduledEventRepository, outgoingEventRepository, scheduledEventMapper, outgoingEventMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public BlockingScheduledEventManager blockingScheduledEventManager(ScheduledOutgoingEventService scheduledOutgoingEventService,
                                                                       BlockingOutgoingEventManagerProperties managerProperties,
                                                                       Map<MessageBrokerEnum, ProduceOutgoingEventService<?>> produceEventServices) {
        return new BlockingScheduledEventManager(scheduledOutgoingEventService, managerProperties, produceEventServices);
    }

    @Bean
    @ConditionalOnMissingBean
    public ProcessScheduledEventScheduler processScheduledEventScheduler(ScheduledEventManager scheduledEventManager) {
        return new ProcessScheduledEventScheduler(scheduledEventManager);
    }

}
