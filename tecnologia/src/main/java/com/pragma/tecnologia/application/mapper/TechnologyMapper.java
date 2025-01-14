package com.pragma.tecnologia.application.mapper;

import com.pragma.tecnologia.application.dto.TechnologyDto;
import com.pragma.tecnologia.domain.model.Technology;
import org.springframework.stereotype.Component;

@Component
public class TechnologyMapper {

    public Technology toDomain(TechnologyDto dto) {
        if (dto == null) {
            return null;
        }
        return Technology.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    public TechnologyDto toDto(Technology technology) {
        if (technology == null) {
            return null;
        }
        TechnologyDto dto = new TechnologyDto();
        dto.setId(technology.getId());
        dto.setName(technology.getName());
        dto.setDescription(technology.getDescription());
        return dto;
    }
}