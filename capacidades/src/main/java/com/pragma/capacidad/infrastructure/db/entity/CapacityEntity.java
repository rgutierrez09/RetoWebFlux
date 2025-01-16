package com.pragma.capacidad.infrastructure.db.entity;

import com.pragma.capacidad.infrastructure.commons.Constants;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(Constants.CAPACITIES_TABLE)
public class CapacityEntity {
    @Id
    private Long id;
    private String name;
    private String description;
}