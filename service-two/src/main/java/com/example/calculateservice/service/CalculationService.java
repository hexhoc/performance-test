package com.example.calculateservice.service;

import com.example.calculateservice.dto.CalculationDto;
import com.example.calculateservice.entity.CalculationEntity;
import com.example.calculateservice.mapper.CalculationMapper;
import com.example.calculateservice.repository.CalculationRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalculationService {

    private final CalculationRepository calculationRepository;
    private final CalculationMapper calculationMapper;

    private CalculationEntity findByIdOrElseThrow(long id) {
        return calculationRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException(id, CalculationEntity.class.getName()));
    }

    @Transactional
    public void createIfNotExist(long id) {
        var calculationOpt = calculationRepository.findById(id);
        if (calculationOpt.isEmpty())
            create(id);
    }

    @Transactional
    public CalculationDto calculateValue(Long id, int valueNum) {
        var calculationEntity = findByIdOrElseThrow(id);
        calculationEntity = calculationRepository.save(calculationEntity);

        switch (valueNum) {
            case 1 -> calculationEntity.setValue1(calculationEntity.getValue1().add(new BigDecimal(1)));
            case 2 -> calculationEntity.setValue2(calculationEntity.getValue2().add(new BigDecimal(1)));
            case 3 -> calculationEntity.setValue3(calculationEntity.getValue3().add(new BigDecimal(1)));
            default -> throw new IllegalStateException("Unexpected value: " + valueNum);
        }

        return calculationMapper.toDto(calculationEntity);
    }

    public CalculationDto create(Long id) {
        CalculationEntity entity = new CalculationEntity();
        entity.setId(id);
        entity.setName("Example name");
        entity.setDescription("Example description");
        entity.setValue1(BigDecimal.ZERO);
        entity.setValue2(BigDecimal.ZERO);
        entity.setValue3(BigDecimal.ZERO);

        entity = calculationRepository.save(entity);
        return calculationMapper.toDto(entity);
    }
}
