package com.example.serviceone.handler;

import com.example.serviceone.config.KafkaConfig;
import com.example.serviceone.constant.EventStatusEnum;
import com.example.serviceone.constant.EventTypeEnum;
import com.example.serviceone.constant.SourceEnum;
import com.example.serviceone.dto.CalculateResponse;
import com.example.serviceone.entity.CalculationEntity;
import com.example.serviceone.entity.IncomingEventEntity;
import com.example.serviceone.entity.OutgoingEventEntity;
import com.example.serviceone.message.Message;
import com.example.serviceone.message.MessageSender;
import com.example.serviceone.service.CalculationService;
import com.example.serviceone.service.IncomingEventService;
import com.example.serviceone.service.OutgoingEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CalculationHandler {
    private final IncomingEventService incomingEventService;
    private final OutgoingEventService outgoingEventService;
    private final CalculationService calculationService;
    private final MessageSender messageSender;
    private final ObjectMapper objectMapper;

    @Transactional(noRollbackFor = Exception.class)
    public void handle() {
        log.info("Handle command: calculate");

        // TODO: Check. transaction should work if exception has appear
        var incomingEvent = incomingEventService.createEvent("", EventTypeEnum.STEP_ONE, SourceEnum.HTTP);
        try {
            var calculationResponse = calculationService.makeCalculation();
            var outgoingEvent = outgoingEventService.createEvent(incomingEvent, objectMapper.writeValueAsString(calculationResponse));
//            sendMessage(outgoingEvent);
        } catch (Exception e) {
            log.error(e.getMessage());
            incomingEvent.setStatus(EventStatusEnum.FAILED);
        }
    }

    private void sendMessage(OutgoingEventEntity outgoingEvent) {
        messageSender.send(outgoingEvent, KafkaConfig.SERVICE_ONE_TOPIC);
    }
}
