package com.pragma.capacidad.application.mapper;


import com.pragma.capacidad.application.dto.CapacityDto;
import com.pragma.capacidad.domain.model.Capacity;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CapacityMapperTest {

    private final CapacityMapper mapper = new CapacityMapper();

    @Test
    void whenMappingToDto_thenSuccess() {
        // Arrange
        List<Long> techIds = Arrays.asList(1L, 2L, 3L);
        Capacity domain = Capacity.builder()
                .id(1L)
                .name("Backend Dev")
                .description("Java development")
                .technologyIds(techIds)
                .build();

        // Act
        CapacityDto dto = mapper.toDto(domain);

        // Assert
        assertNotNull(dto);
        assertEquals(domain.getId(), dto.getId());
        assertEquals(domain.getName(), dto.getName());
        assertEquals(domain.getDescription(), dto.getDescription());
        assertEquals(domain.getTechnologyIds(), dto.getTechnologyIds());
    }

    @Test
    void whenMappingToDomain_thenSuccess() {
        // Arrange
        List<Long> techIds = Arrays.asList(1L, 2L, 3L);
        CapacityDto dto = new CapacityDto();
        dto.setId(1L);
        dto.setName("Backend Dev");
        dto.setDescription("Java development");
        dto.setTechnologyIds(techIds);

        // Act
        Capacity domain = mapper.toDomain(dto);

        // Assert
        assertNotNull(domain);
        assertEquals(dto.getId(), domain.getId());
        assertEquals(dto.getName(), domain.getName());
        assertEquals(dto.getDescription(), domain.getDescription());
        assertEquals(dto.getTechnologyIds(), domain.getTechnologyIds());
    }

    @Test
    void whenMappingNull_thenReturnNull() {
        assertNull(mapper.toDto(null));
        assertNull(mapper.toDomain(null));
    }
}