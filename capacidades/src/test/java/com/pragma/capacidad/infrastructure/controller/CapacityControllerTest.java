package com.pragma.capacidad.infrastructure.controller;

import com.pragma.capacidad.application.dto.CapacityDto;
import com.pragma.capacidad.application.dto.PagedResponseDto;
import com.pragma.capacidad.application.mapper.CapacityMapper;
import com.pragma.capacidad.application.usecase.CreateCapacityUseCase;
import com.pragma.capacidad.application.usecase.ListCapacityUseCase;
import com.pragma.capacidad.domain.model.Capacity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CapacityControllerTest {

    @Mock
    private CreateCapacityUseCase createCapacityUseCase;

    @Mock
    private ListCapacityUseCase listCapacityUseCase;

    @Mock
    private CapacityMapper capacityMapper;

    @InjectMocks
    private CapacityController controller;

    private WebTestClient webTestClient;
    private CapacityDto capacityDto;
    private Capacity capacity;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(controller).build();

        capacityDto = new CapacityDto();
        capacityDto.setName("Backend Developer");
        capacityDto.setDescription("Java Development");
        capacityDto.setTechnologyNames(Arrays.asList("Java", "Spring"));

        capacity = Capacity.builder()
                .name("Backend Developer")
                .description("Java Development")
                .technologyNames(Arrays.asList("Java", "Spring"))
                .build();
    }

    @Test
    void whenCreateCapacity_thenSuccess() {
        // Arrange
        when(capacityMapper.toDomain(any())).thenReturn(capacity);
        when(createCapacityUseCase.execute(any(), any())).thenReturn(Mono.just(capacity));
        when(capacityMapper.toDto(any())).thenReturn(capacityDto);

        // Act & Assert
        webTestClient.post()
                .uri("/webflux/v1/capacidades")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(capacityDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CapacityDto.class)
                .value(dto -> {
                    assert dto.getName().equals(capacityDto.getName());
                    assert dto.getDescription().equals(capacityDto.getDescription());
                });
    }

    @Test
    void whenListCapacities_thenSuccess() {
        // Arrange
        PagedResponseDto<Capacity> domainResponse = PagedResponseDto.<Capacity>builder()
                .content(List.of(capacity))
                .pageNumber(0)
                .pageSize(10)
                .totalElements(1)
                .totalPages(1)
                .isLastPage(true)
                .build();

        PagedResponseDto<CapacityDto> dtoResponse = PagedResponseDto.<CapacityDto>builder()
                .content(List.of(capacityDto))
                .pageNumber(0)
                .pageSize(10)
                .totalElements(1)
                .totalPages(1)
                .isLastPage(true)
                .build();

        when(listCapacityUseCase.execute(
                eq("nombre"),
                eq("asc"),
                eq(0),
                eq(10)))
                .thenReturn(Mono.just(domainResponse));
        when(capacityMapper.toDto(any(Capacity.class))).thenReturn(capacityDto);

        // Act & Assert
        webTestClient.get()
                .uri("/webflux/v1/capacidades?sortBy=nombre&sortOrder=asc&page=0&size=10")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.content").isArray()
                .jsonPath("$.pageNumber").isEqualTo(0)
                .jsonPath("$.pageSize").isEqualTo(10)
                .jsonPath("$.totalElements").isEqualTo(1)
                .jsonPath("$.totalPages").isEqualTo(1)
                .jsonPath("$.lastPage").isEqualTo(true);
    }
}