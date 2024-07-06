package com.example.calculateservice.repository;

import com.example.calculateservice.entity.CalculationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculationRepository extends JpaRepository<CalculationEntity, Long>{
    
}
