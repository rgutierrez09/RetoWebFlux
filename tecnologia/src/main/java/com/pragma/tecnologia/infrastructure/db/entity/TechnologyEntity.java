package com.pragma.tecnologia.infrastructure.db.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("tecnologias")
public class TechnologyEntity {
    @Id
    private Long id;

    private String name;
    private String description;
}
