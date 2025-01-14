package com.pragma.tecnologia.application.usecase;

import com.pragma.tecnologia.application.dto.PagedResponseDto;
import com.pragma.tecnologia.domain.model.Technology;
import com.pragma.tecnologia.domain.repository.ITechnologyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListTechnologyUseCaseTest {

    @Mock
    private ITechnologyRepository repository;

    private ListTechnologyUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ListTechnologyUseCase(repository);
    }

    @Test
    void execute_Success_AscendingOrder() {
        // Arrange
        int page = 0;
        int size = 10;

        Technology tech1 = Technology.builder()
                .id(1L)
                .name("Java")
                .description("Programming Language")
                .build();

        Technology tech2 = Technology.builder()
                .id(2L)
                .name("Python")
                .description("Programming Language")
                .build();

        when(repository.countTechnologies()).thenReturn(Mono.just(2L));
        when(repository.findAllOrderedByNameAsc(any(PageRequest.class)))
                .thenReturn(Flux.fromIterable(List.of(tech1, tech2)));

        // Act & Assert
        StepVerifier.create(useCase.execute("asc", page, size))
                .expectNextMatches(pagedResponse ->
                        pagedResponse.getContent().size() == 2 &&
                                pagedResponse.getPageNumber() == 0 &&
                                pagedResponse.getPageSize() == 10 &&
                                pagedResponse.getTotalElements() == 2 &&
                                pagedResponse.getTotalPages() == 1 &&
                                pagedResponse.isLastPage()
                )
                .verifyComplete();
    }

    @Test
    void execute_Success_DescendingOrder() {
        // Arrange
        int page = 0;
        int size = 10;

        Technology tech1 = Technology.builder()
                .id(1L)
                .name("Python")
                .description("Programming Language")
                .build();

        Technology tech2 = Technology.builder()
                .id(2L)
                .name("Java")
                .description("Programming Language")
                .build();

        when(repository.countTechnologies()).thenReturn(Mono.just(2L));
        when(repository.findAllOrderedByNameDesc(any(PageRequest.class)))
                .thenReturn(Flux.fromIterable(List.of(tech1, tech2)));

        // Act & Assert
        StepVerifier.create(useCase.execute("desc", page, size))
                .expectNextMatches(pagedResponse ->
                        pagedResponse.getContent().size() == 2 &&
                                pagedResponse.getPageNumber() == 0 &&
                                pagedResponse.getPageSize() == 10 &&
                                pagedResponse.getTotalElements() == 2 &&
                                pagedResponse.getTotalPages() == 1 &&
                                pagedResponse.isLastPage()
                )
                .verifyComplete();
    }

    @Test
    void execute_InvalidSortOrder() {
        // Act & Assert
        StepVerifier.create(useCase.execute("invalid", 0, 10))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void execute_EmptyPage() {
        // Arrange
        int page = 0;
        int size = 10;

        when(repository.countTechnologies()).thenReturn(Mono.just(0L));
        when(repository.findAllOrderedByNameAsc(any(PageRequest.class)))
                .thenReturn(Flux.empty());

        // Act & Assert
        StepVerifier.create(useCase.execute("asc", page, size))
                .expectNextMatches(pagedResponse ->
                        pagedResponse.getContent().isEmpty() &&
                                pagedResponse.getTotalElements() == 0 &&
                                pagedResponse.getTotalPages() == 0 &&
                                pagedResponse.isLastPage()
                )
                .verifyComplete();
    }
}