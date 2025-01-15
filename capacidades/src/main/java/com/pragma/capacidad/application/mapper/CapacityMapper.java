package com.pragma.capacidad.application.mapper;

import com.pragma.capacidad.application.dto.CapacityDto;
import com.pragma.capacidad.domain.model.Capacity;
import org.springframework.stereotype.Component;

@Component
public class CapacityMapper {

    public Capacity toDomain(CapacityDto dto) {
        if (dto == null) {
            return null;
        }
        return Capacity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .technologyIds(dto.getTechnologyIds())
                .build();
    }

    public CapacityDto toDto(Capacity domain) {
        if (domain == null) {
            return null;
        }
        CapacityDto dto = new CapacityDto();
        dto.setId(domain.getId());
        dto.setName(domain.getName());
        dto.setDescription(domain.getDescription());
        dto.setTechnologyIds(domain.getTechnologyIds());
        return dto;
    }
}