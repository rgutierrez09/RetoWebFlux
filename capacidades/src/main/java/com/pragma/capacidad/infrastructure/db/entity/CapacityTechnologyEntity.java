package com.pragma.capacidad.infrastructure.db.entity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("capacidad_tecnologias")
public class CapacityTechnologyEntity {
    private Long capacidadId;
    private Long technologyId;
}