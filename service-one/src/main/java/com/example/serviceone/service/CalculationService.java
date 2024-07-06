package com.example.serviceone.service;

import com.example.serviceone.dto.CalculationDto;
import com.example.serviceone.entity.CalculationEntity;
import com.example.serviceone.mapper.CalculationMapper;
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
    private final CalculationMapper calculationMapper;
    private final Random random = new Random();

    @Transactional
    public CalculationDto findAny() {
        var count = calculationRepository.count();
        var randomId = Long.valueOf(random.nextInt((int) (count + 1)));
        var calculationOpt = calculationRepository.findById((randomId));
        var calculationEntity = calculationOpt.orElseThrow(() -> new ObjectNotFoundException(randomId, CalculationEntity.class.getName()));
        return calculationMapper.toDto(calculationEntity);
    }

    @Transactional
    public CalculationDto increaseValue(Long id) {
        var calculationOpt = calculationRepository.findById(id);
        var calculationEntity = calculationOpt.orElseThrow(() -> new ObjectNotFoundException(id, CalculationEntity.class.getName()));

        calculationEntity.setValue(calculationEntity.getValue().add(new BigDecimal(1)));
        calculationEntity = calculationRepository.save(calculationEntity);

        return calculationMapper.toDto(calculationEntity);
    }

    public void update(CalculationDto payload) {
        var entity = calculationMapper.toEntity(payload);
        calculationRepository.save(entity);
    }
}
