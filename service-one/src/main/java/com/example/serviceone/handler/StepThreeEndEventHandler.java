package com.example.serviceone.handler;

import com.example.serviceone.message.event.StepThreeEndEvent;
import com.example.serviceone.service.IncomingEventService;
import com.example.serviceone.service.OperationService;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StepThreeEndEventHandler {
    private final IncomingEventService incomingEventService;
    private final OperationService operationService;

    @EventListener
    @Timed(value = "step.three.end.handler.time")
    public void handle(StepThreeEndEvent stepThreeEndEvent) {
        log.info("Handle event: STEP 3 END");
        var incomingEvent = incomingEventService.createEvent(
            stepThreeEndEvent.getPayload(),
            stepThreeEndEvent.getTraceId(),
            stepThreeEndEvent.getRequestId(),
            stepThreeEndEvent.getFrom(),
            stepThreeEndEvent.getEventType(),
            CalculationDto.class);
        try {
            operationService.update(incomingEvent.getPayload());
            incomingEventService.saveWithSuccess(incomingEvent);
        } catch (Exception e) {
            log.error(e.getMessage());
            incomingEventService.saveWithError(incomingEvent);
        }
    }
}
