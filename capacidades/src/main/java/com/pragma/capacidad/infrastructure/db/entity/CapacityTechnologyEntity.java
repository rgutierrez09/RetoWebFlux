package com.pragma.capacidad.infrastructure.db.entity;

import com.pragma.capacidad.infrastructure.commons.Constants;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(Constants.CAPACITY_TECHNOLOGIES_TABLE)
public class CapacityTechnologyEntity {
    private Long capacidadId;
    private Long technologyId;
    private String technologyName;
}