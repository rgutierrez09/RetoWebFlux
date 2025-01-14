package com.pragma.tecnologia.application.mapper;

import com.pragma.tecnologia.application.dto.TechnologyDto;
import com.pragma.tecnologia.domain.model.Technology;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TechnologyMapperTest {

    private TechnologyMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TechnologyMapper();
    }

    @Test
    void toDomain_Success() {
        TechnologyDto dto = new TechnologyDto();
        dto.setId(1L);
        dto.setName("Java");
        dto.setDescription("Programming Language");

        Technology result = mapper.toDomain(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Java", result.getName());
        assertEquals("Programming Language", result.getDescription());
    }

    @Test
    void toDomain_NullDto() {
        Technology result = mapper.toDomain(null);

        assertNull(result);
    }

    @Test
    void toDto_Success() {
        Technology domain = Technology.builder()
                .id(1L)
                .name("Java")
                .description("Programming Language")
                .build();

        TechnologyDto result = mapper.toDto(domain);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Java", result.getName());
        assertEquals("Programming Language", result.getDescription());
    }

    @Test
    void toDto_NullDomain() {
        TechnologyDto result = mapper.toDto(null);
        assertNull(result);
    }
}