package com.pragma.capacidad.infrastructure.controller;

import com.pragma.capacidad.application.dto.CapacityDto;
import com.pragma.capacidad.domain.model.Capacity;
import com.pragma.capacidad.domain.repository.ICapacityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@SpringBootTest
@AutoConfigureWebTestClient
class CapacityControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ICapacityRepository repository;

    @Test
    void whenCreatingValidCapacity_thenSuccess() {
        // Arrange
        CapacityDto dto = new CapacityDto();
        dto.setName("Test Capacity");
        dto.setDescription("Test Description");
        dto.setTechnologyIds(Arrays.asList(1L, 2L, 3L));

        // Act & Assert
        webTestClient.post()
                .uri("/webflux/v1/capacidades")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(dto), CapacityDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Test Capacity")
                .jsonPath("$.description").isEqualTo("Test Description")
                .jsonPath("$.technologyIds").isArray()
                .jsonPath("$.technologyIds.length()").isEqualTo(3);
    }

    @Test
    void whenCreatingInvalidCapacity_thenError() {
        // Arrange
        CapacityDto dto = new CapacityDto();
        dto.setName("Test Capacity");
        dto.setDescription("Test Description");
        dto.setTechnologyIds(Arrays.asList(1L, 2L)); // Menos de 3 tecnologías

        // Act & Assert
        webTestClient.post()
                .uri("/webflux/v1/capacidades")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(dto), CapacityDto.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void whenListingCapacities_thenSuccess() {
        // Act & Assert
        webTestClient.get()
                .uri("/webflux/v1/capacidades?sortBy=nombre&sortOrder=asc&page=0&size=10")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.content").isArray()
                .jsonPath("$.pageNumber").isEqualTo(0)
                .jsonPath("$.pageSize").isEqualTo(10)
                .jsonPath("$.totalElements").isNumber()
                .jsonPath("$.totalPages").isNumber()
                .jsonPath("$.isLastPage").isBoolean();
    }

    @Test
    void whenListingWithInvalidParameters_thenError() {
        // Act & Assert
        webTestClient.get()
                .uri("/webflux/v1/capacidades?sortBy=invalid&sortOrder=asc")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void whenListingByTechCount_thenSuccess() {
        // Act & Assert
        webTestClient.get()
                .uri("/webflux/v1/capacidades?sortBy=techCount&sortOrder=desc")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.content").isArray()
                .jsonPath("$.pageNumber").isEqualTo(0)
                .jsonPath("$.pageSize").isEqualTo(10);
    }
}