package com.example.calculateservice.service;

import com.example.calculateservice.dto.CalculationDto;
import com.example.calculateservice.entity.CalculationEntity;
import com.example.calculateservice.mapper.CalculationMapper;
import com.example.calculateservice.repository.CalculationRepository;
import com.example.calculateservice.utils.HighloadUtil;
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
        HighloadUtil.randomCase();
        return calculationRepository.findById(id).map(calculationMapper::toDto);
    }

    @Transactional
    public CalculationDto create(Long id) {
        HighloadUtil.randomCase();

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

    @Transactional
    public void update(CalculationDto payload) {
        HighloadUtil.randomCase();
        var entity = calculationMapper.toEntity(payload);
        calculationRepository.save(entity);
    }
}
