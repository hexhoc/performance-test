package com.example.transactionalbox.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "transactionalbox.liquibase")
public class TransactionalBoxLiquibaseProperties {

    /**
     * Whether to run Liquibase for tbox
     */
    private boolean enabled = true;

    /**
     * Liquibase changelog location for tbox
     */
    private String changeLog = "classpath:/db/changelog/db.changelog-transactional-box.xml";

    /**
     * Target schema for tbox changes
     */
    private String defaultSchema = "tbox";

    private String contexts;

}