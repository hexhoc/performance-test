package com.example.serviceone.repository;

import com.example.serviceone.entity.OperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OperationRepository extends JpaRepository<OperationEntity, UUID>{
    
}
