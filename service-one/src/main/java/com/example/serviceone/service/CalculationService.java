package com.example.serviceone.service;

import com.example.serviceone.config.KafkaConfig;
import com.example.serviceone.dto.CalculateResponse;
import com.example.serviceone.entity.CalculationEntity;
import com.example.serviceone.message.Message;
import com.example.serviceone.message.MessageSender;
import com.example.serviceone.repository.CalculationRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalculationService {

    private final CalculationRepository calculationRepository;
    private final Random random = new Random();

    @Transactional
    public CalculateResponse makeCalculation() {
        var count = calculationRepository.count();
        var randomId = Long.valueOf(random.nextInt((int) (count + 1)));
        var calculationOpt = calculationRepository.findById((randomId));
        var calculation = calculationOpt.orElseThrow(() -> new ObjectNotFoundException(randomId, CalculationEntity.class.getName()));

        calculation.setValue(calculation.getValue().add(new BigDecimal(1)));
        calculation = calculationRepository.save(calculation);

        return new CalculateResponse(
            calculation.getId(),
            calculation.getName(),
            calculation.getDescription(),
            calculation.getValue());
    }
}
