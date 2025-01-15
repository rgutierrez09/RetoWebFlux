package com.pragma.tecnologia.infrastructure.controller;

import com.pragma.tecnologia.application.dto.PagedResponseDto;
import com.pragma.tecnologia.application.dto.TechnologyDto;
import com.pragma.tecnologia.application.mapper.TechnologyMapper;
import com.pragma.tecnologia.application.usecase.CreateTechnologyUseCase;
import com.pragma.tecnologia.application.usecase.ListTechnologyUseCase;
import com.pragma.tecnologia.domain.exception.DuplicatedTechnologyNameException;
import com.pragma.tecnologia.domain.model.Technology;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TechnologyControllerTest {

    @Mock
    private CreateTechnologyUseCase createTechnologyUseCase;

    @Mock
    private ListTechnologyUseCase listTechnologyUseCase;

    @Mock
    private TechnologyMapper technologyMapper;

    @InjectMocks
    private TechnologyController controller;

    @Test
    void listTechnologies_Success() {
        // Arrange
        Technology tech1 = Technology.builder()
                .id(1L)
                .name("Java")
                .description("lenguaje de programacion")
                .build();

        TechnologyDto dto1 = new TechnologyDto();
        dto1.setId(1L);
        dto1.setName("Java");
        dto1.setDescription("lenguaje de programacion");

        PagedResponseDto<Technology> domainResponse = PagedResponseDto.<Technology>builder()
                .content(List.of(tech1))
                .pageNumber(0)
                .pageSize(10)
                .totalElements(1)
                .totalPages(1)
                .isLastPage(true)
                .build();

        when(listTechnologyUseCase.execute(anyString(), anyInt(), anyInt()))
                .thenReturn(Mono.just(domainResponse));
        when(technologyMapper.toDto(tech1)).thenReturn(dto1);

        // Act & Assert
        StepVerifier.create(controller.listTechnologies("asc", 0, 10))
                .expectNextMatches(response ->
                        response.getContent().size() == 1 &&
                                response.getPageNumber() == 0 &&
                                response.getPageSize() == 10 &&
                                response.getTotalElements() == 1 &&
                                response.getTotalPages() == 1 &&
                                response.isLastPage() &&
                                response.getContent().get(0).equals(dto1)
                )
                .verifyComplete();
    }

    @Test
    void createTechnology_Success() {
        // Arrange
        TechnologyDto inputDto = new TechnologyDto();
        inputDto.setName("Java");
        inputDto.setDescription("lenguaje de programacion");

        Technology domain = Technology.builder()
                .name("Java")
                .description("lenguaje de programacion")
                .build();

        Technology savedDomain = Technology.builder()
                .id(1L)
                .name("Java")
                .description("lenguaje de programacion")
                .build();

        TechnologyDto outputDto = new TechnologyDto();
        outputDto.setId(1L);
        outputDto.setName("Java");
        outputDto.setDescription("lenguaje de programacion");

        when(technologyMapper.toDomain(inputDto)).thenReturn(domain);
        when(createTechnologyUseCase.execute(domain)).thenReturn(Mono.just(savedDomain));
        when(technologyMapper.toDto(savedDomain)).thenReturn(outputDto);

        // Act & Assert
        StepVerifier.create(controller.createTechnology(inputDto))
                .expectNext(outputDto)
                .verifyComplete();
    }

    @Test
    void createTechnology_DuplicateName() {
        // Arrange
        TechnologyDto inputDto = new TechnologyDto();
        inputDto.setName("Java");
        inputDto.setDescription("lenguaje de programacion");

        Technology domain = Technology.builder()
                .name("Java")
                .description("lenguaje de programacion")
                .build();

        when(technologyMapper.toDomain(inputDto)).thenReturn(domain);
        when(createTechnologyUseCase.execute(domain))
                .thenReturn(Mono.error(new DuplicatedTechnologyNameException("El nombre de la tecnologia ya existe: Java")));

        // Act & Assert
        StepVerifier.create(controller.createTechnology(inputDto))
                .expectError(DuplicatedTechnologyNameException.class)
                .verify();
    }

    @Test
    void findByName_Success() {
        // Arrange
        String techName = "Java";
        Technology domain = Technology.builder()
                .id(1L)
                .name("Java")
                .description("lenguaje de programacion")
                .build();

        TechnologyDto dto = new TechnologyDto();
        dto.setId(1L);
        dto.setName("Java");
        dto.setDescription("lenguaje de programacion");

        when(listTechnologyUseCase.findByName(techName)).thenReturn(Mono.just(domain));
        when(technologyMapper.toDto(domain)).thenReturn(dto);

        // Act & Assert
        StepVerifier.create(controller.findByName(techName))
                .expectNext(dto)
                .verifyComplete();
    }

    @Test
    void findByName_NotFound() {
        // Arrange
        String techName = "NonExistentTech";
        when(listTechnologyUseCase.findByName(techName)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(controller.findByName(techName))
                .expectComplete()
                .verify();
    }

    @Test
    void listTechnologies_EmptyList() {
        // Arrange
        PagedResponseDto<Technology> emptyResponse = PagedResponseDto.<Technology>builder()
                .content(List.of())
                .pageNumber(0)
                .pageSize(10)
                .totalElements(0)
                .totalPages(0)
                .isLastPage(true)
                .build();

        when(listTechnologyUseCase.execute(anyString(), anyInt(), anyInt()))
                .thenReturn(Mono.just(emptyResponse));

        // Act & Assert
        StepVerifier.create(controller.listTechnologies("asc", 0, 10))
                .expectNextMatches(response ->
                        response.getContent().isEmpty() &&
                                response.getPageNumber() == 0 &&
                                response.getPageSize() == 10 &&
                                response.getTotalElements() == 0 &&
                                response.getTotalPages() == 0 &&
                                response.isLastPage()
                )
                .verifyComplete();
    }

    @Test
    void listTechnologies_InvalidSortOrder() {
        // Arrange
        when(listTechnologyUseCase.execute("invalid", 0, 10))
                .thenReturn(Mono.error(new IllegalArgumentException("Sort order must be 'asc' or 'desc'")));

        // Act & Assert
        StepVerifier.create(controller.listTechnologies("invalid", 0, 10))
                .expectError(IllegalArgumentException.class)
                .verify();
    }
}