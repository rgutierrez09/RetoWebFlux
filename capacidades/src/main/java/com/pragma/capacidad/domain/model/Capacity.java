package com.pragma.capacidad.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Capacity {
    private Long id;
    private String name;
    private String description;
    private List<Long> technologyIds;
    private List<String> technologyNames;
}
