package com.example.servicetwo.repository;

import com.example.servicetwo.entity.ContainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContainerRepository extends JpaRepository<ContainerEntity, Long>{
    
}
