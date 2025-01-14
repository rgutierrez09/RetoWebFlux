package com.pragma.tecnologia.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Technology {
    private Long id;
    private String name;
    private String description;
}
