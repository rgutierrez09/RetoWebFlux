package com.pragma.capacidad.application.usecase;

import com.pragma.capacidad.domain.model.Capacity;
import com.pragma.capacidad.domain.repository.ICapacityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ListCapacityUseCaseTest {

    @Mock
    private ICapacityRepository repository;

    @InjectMocks
    private ListCapacityUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenListingByNameAsc_thenSuccess() {
        // Arrange
        Capacity capacity1 = Capacity.builder().name("A").build();
        Capacity capacity2 = Capacity.builder().name("B").build();

        when(repository.countCapacities()).thenReturn(Mono.just(2L));
        when(repository.findAllOrderByNameAsc(any(PageRequest.class)))
                .thenReturn(Flux.fromIterable(Arrays.asList(capacity1, capacity2)));

        // Act & Assert
        StepVerifier.create(useCase.execute("nombre", "asc", 0, 10))
                .expectNextMatches(response ->
                        response.getContent().size() == 2 &&
                                response.getTotalElements() == 2L &&
                                response.getPageNumber() == 0
                )
                .verifyComplete();
    }

    @Test
    void whenInvalidSortOrder_thenError() {
        // Act & Assert
        StepVerifier.create(useCase.execute("nombre", "invalid", 0, 10))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void whenInvalidSortBy_thenError() {
        // Act & Assert
        StepVerifier.create(useCase.execute("invalid", "asc", 0, 10))
                .expectError(IllegalArgumentException.class)
                .verify();
    }
}