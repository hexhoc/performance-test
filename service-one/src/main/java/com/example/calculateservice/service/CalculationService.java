package com.example.calculateservice.service;

import com.example.calculateservice.dto.CalculationDto;
import com.example.calculateservice.entity.CalculationEntity;
import com.example.calculateservice.mapper.CalculationMapper;
import com.example.calculateservice.repository.CalculationRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalculationService {

    private final CalculationRepository calculationRepository;
    private final CalculationMapper calculationMapper;

    @Transactional
    public Optional<CalculationDto> findById(Long id) {
        return calculationRepository.findById(id).map(calculationMapper::toDto);
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

    public void update(CalculationDto payload) {
        var entity = calculationMapper.toEntity(payload);
        calculationRepository.save(entity);
    }
}
