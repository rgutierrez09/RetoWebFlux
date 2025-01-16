package com.pragma.capacidad.application.usecase;

import com.pragma.capacidad.domain.model.Capacity;
import com.pragma.capacidad.domain.repository.ICapacityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListCapacityUseCaseTest {

    @Mock
    private ICapacityRepository repository;

    @InjectMocks
    private ListCapacityUseCase useCase;

    private List<Capacity> capacities;
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @BeforeEach
    void setUp() {
        capacities = Arrays.asList(
                Capacity.builder()
                        .id(1L)
                        .name("Backend Dev")
                        .description("Java Development")
                        .technologyNames(Arrays.asList("Java", "Spring"))
                        .build(),
                Capacity.builder()
                        .id(2L)
                        .name("Frontend Dev")
                        .description("Web Development")
                        .technologyNames(Arrays.asList("JavaScript", "React"))
                        .build()
        );
    }

    @Test
    void whenListCapacitiesOrderByNameAsc_thenSuccess() {
        // Arrange
        when(repository.countCapacities()).thenReturn(Mono.just(2L));
        when(repository.findAllOrderByNameAsc(any(PageRequest.class)))
                .thenReturn(Flux.fromIterable(capacities));

        // Act & Assert
        StepVerifier.create(useCase.execute("nombre", "asc", PAGE, SIZE))
                .expectNextMatches(response -> {
                    // Verificar tamaño y contenido de la lista
                    if (response.getContent().size() != 2) {
                        return false;
                    }
                    Capacity firstCapacity = response.getContent().get(0);
                    Capacity secondCapacity = response.getContent().get(1);

                    // Verificar el orden alfabético
                    if (!firstCapacity.getName().equals("Backend Dev") ||
                            !secondCapacity.getName().equals("Frontend Dev")) {
                        return false;
                    }

                    // Verificar información de paginación
                    return response.getPageNumber() == PAGE &&
                            response.getPageSize() == SIZE &&
                            response.getTotalElements() == 2 &&
                            response.getTotalPages() == 1 &&
                            response.isLastPage();
                })
                .verifyComplete();
    }

    @Test
    void whenListCapacitiesOrderByNameDesc_thenSuccess() {
        // Arrange
        when(repository.countCapacities()).thenReturn(Mono.just(2L));
        when(repository.findAllOrderByNameDesc(any(PageRequest.class)))
                .thenReturn(Flux.fromIterable(capacities));

        // Act & Assert
        StepVerifier.create(useCase.execute("nombre", "desc", PAGE, SIZE))
                .expectNextMatches(response ->
                        response.getContent().size() == 2 &&
                                response.getTotalElements() == 2 &&
                                response.isLastPage()
                )
                .verifyComplete();
    }

    @Test
    void whenInvalidSortOrder_thenError() {
        // Act & Assert
        StepVerifier.create(useCase.execute("nombre", "invalid", PAGE, SIZE))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void whenEmptyRepository_thenReturnEmptyPage() {
        // Arrange
        when(repository.countCapacities()).thenReturn(Mono.just(0L));
        when(repository.findAllOrderByNameAsc(any(PageRequest.class)))
                .thenReturn(Flux.empty());

        // Act & Assert
        StepVerifier.create(useCase.execute("nombre", "asc", PAGE, SIZE))
                .expectNextMatches(response ->
                        response.getContent().isEmpty() &&
                                response.getTotalElements() == 0 &&
                                response.getTotalPages() == 0 &&
                                response.isLastPage()
                )
                .verifyComplete();
    }
}
