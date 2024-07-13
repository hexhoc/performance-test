package com.example.calculateservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "calculation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalculationEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name  = "name")
    private String name;

    @Column(name  = "description")
    private String description;

    @Column(name   = "value_1")
    private BigDecimal value1;

    @Column(name   = "value_2")
    private BigDecimal value2;

    @Column(name   = "value_3")
    private BigDecimal value3;

}
