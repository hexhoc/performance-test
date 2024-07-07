package com.example.calculateservice.service;

import com.example.calculateservice.dto.CalculationDto;
import com.example.calculateservice.entity.CalculationEntity;
import com.example.calculateservice.mapper.CalculationMapper;
import com.example.calculateservice.repository.CalculationRepository;
import jakarta.transaction.Transactional;
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
    public CalculationDto findById(Long id) {
        return calculationMapper.toDto(findByIdOrElseThrow(id));
    }

    private CalculationEntity findByIdOrElseThrow(long id) {
        return calculationRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException(id, CalculationEntity.class.getName()));
    }

    @Transactional
    public CalculationDto findAny() {
        var count = calculationRepository.count();
        var randomId = Long.valueOf(random.nextInt((int) (count + 1)));
        var calculationOpt = calculationRepository.findById((randomId));
        var calculationEntity = calculationOpt.orElseThrow(() -> new ObjectNotFoundException(randomId, CalculationEntity.class.getName()));
        return calculationMapper.toDto(calculationEntity);
    }

    public void update(CalculationDto payload) {
        var entity = calculationMapper.toEntity(payload);
        calculationRepository.save(entity);
    }
}
