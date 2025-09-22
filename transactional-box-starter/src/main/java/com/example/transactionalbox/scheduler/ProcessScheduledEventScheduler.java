package com.example.transactionalbox.scheduler;

import com.example.transactionalbox.manager.ScheduledEventManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessScheduledEventScheduler {
    private final ScheduledEventManager scheduledEventManager;

    @Scheduled(fixedRateString = "${transactional-box.scheduler.fixed-rate:1000}")
    public void process() {
        try {
            scheduledEventManager.sendScheduledEvents();
        } catch (Exception e) {
            log.error("Error during sending scheduled events", e);
        }
    }
}
