package com.example.serviceone.repository;

import com.example.serviceone.entity.CalculationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculationRepository extends JpaRepository<CalculationEntity, Long>{
    
}
