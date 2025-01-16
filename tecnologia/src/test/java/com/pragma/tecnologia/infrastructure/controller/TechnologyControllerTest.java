package com.pragma.tecnologia.infrastructure.controller;

import com.pragma.tecnologia.application.dto.TechnologyDto;
import com.pragma.tecnologia.application.mapper.TechnologyMapper;
import com.pragma.tecnologia.application.usecase.CreateTechnologyUseCase;
import com.pragma.tecnologia.application.usecase.ListTechnologyUseCase;
import com.pragma.tecnologia.domain.model.Technology;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(TechnologyController.class)
class TechnologyControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private CreateTechnologyUseCase createTechnologyUseCase;

    @MockBean
    private ListTechnologyUseCase listTechnologyUseCase;

    @MockBean
    private TechnologyMapper technologyMapper;

    @Test
    void whenCreateTechnology_thenSuccess() {
        TechnologyDto dto = new TechnologyDto();
        dto.setName("Java");
        dto.setDescription("Programming Language");

        Technology technology = Technology.builder()
                .name("Java")
                .description("Programming Language")
                .build();

        when(technologyMapper.toDomain(any(TechnologyDto.class))).thenReturn(technology);
        when(createTechnologyUseCase.execute(any(Technology.class))).thenReturn(Mono.just(technology));
        when(technologyMapper.toDto(any(Technology.class))).thenReturn(dto);

        webClient.post()
                .uri("/webflux/v1/tecnologias")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Java")
                .jsonPath("$.description").isEqualTo("Programming Language");
    }

    @Test
    void whenInvalidInput_thenBadRequest() {
        TechnologyDto dto = new TechnologyDto();
        // Missing required fields

        webClient.post()
                .uri("/webflux/v1/tecnologias")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isBadRequest();
    }
}