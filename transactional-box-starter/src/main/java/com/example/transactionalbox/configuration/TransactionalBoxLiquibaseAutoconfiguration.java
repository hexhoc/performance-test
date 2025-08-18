package com.example.transactionalbox.configuration;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
@ConditionalOnClass(SpringLiquibase.class)
@AutoConfigureAfter(LiquibaseAutoConfiguration.class)
@EnableConfigurationProperties(TransactionalBoxLiquibaseProperties.class)
@ConditionalOnProperty(prefix = "transactionalbox.liquibase", name = "enabled", havingValue = "true", matchIfMissing = true)
public class TransactionalBoxLiquibaseAutoconfiguration {

    private final TransactionalBoxLiquibaseProperties properties;

    public TransactionalBoxLiquibaseAutoconfiguration(TransactionalBoxLiquibaseProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean(name = "transactionalBoxLiquibase")
    public SpringLiquibase transactionalBoxLiquibase(DataSource dataSource) {
        // create schema if missing (Postgres syntax). Adjust for other DBs.
        try (Connection conn = dataSource.getConnection();
             Statement st = conn.createStatement()) {
            st.execute("CREATE SCHEMA IF NOT EXISTS tbox");
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create schema tbox", e);
        }

        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(properties.getChangeLog());
        liquibase.setDefaultSchema(properties.getDefaultSchema());
        liquibase.setContexts(properties.getContexts());
        liquibase.setShouldRun(properties.isEnabled());
        // you may also set liquibase.setDropFirst(properties.isDropFirst());
        return liquibase;
    }
}
