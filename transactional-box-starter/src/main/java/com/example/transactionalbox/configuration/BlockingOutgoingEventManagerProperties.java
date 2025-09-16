package com.example.transactionalbox.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("transactional-box.blocking-manager")
public class BlockingOutgoingEventManagerProperties {
    private Integer sliceSize = 1000;
}