package com.pragma.capacidad.infrastructure.db.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("capacidades")
public class CapacityEntity {
    @Id
    private Long id;
    private String name;
    private String description;
}