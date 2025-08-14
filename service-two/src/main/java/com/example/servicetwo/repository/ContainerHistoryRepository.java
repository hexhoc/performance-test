package com.example.servicetwo.repository;

import com.example.servicetwo.entity.ContainerHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContainerHistoryRepository extends JpaRepository<ContainerHistoryEntity, UUID>{
    
}
