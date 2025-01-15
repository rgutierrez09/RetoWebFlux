package com.pragma.capacidad.application.usecase;
import com.pragma.capacidad.domain.exception.DuplicatedCapacityNameException;
import com.pragma.capacidad.domain.exception.InvalidCapacityException;
import com.pragma.capacidad.domain.model.Capacity;
import com.pragma.capacidad.domain.repository.ICapacityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CreateCapacityUseCaseTest {

    @Mock
    private ICapacityRepository repository;

    @InjectMocks
    private CreateCapacityUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenValidCapacity_thenSuccess() {
        // Arrange
        Capacity capacity = Capacity.builder()
                .name("Backend Dev")
                .description("Java development")
                .technologyIds(Arrays.asList(1L, 2L, 3L))
                .build();

        when(repository.existsByName(any())).thenReturn(Mono.just(false));
        when(repository.save(any())).thenReturn(Mono.just(capacity));

        // Act & Assert
        StepVerifier.create(useCase.execute(capacity))
                .expectNext(capacity)
                .verifyComplete();
    }

    @Test
    void whenLessThan3Technologies_thenError() {
        // Arrange
        Capacity capacity = Capacity.builder()
                .name("Backend Dev")
                .description("Java development")
                .technologyIds(Arrays.asList(1L, 2L))
                .build();

        // Act & Assert
        StepVerifier.create(useCase.execute(capacity))
                .expectError(InvalidCapacityException.class)
                .verify();
    }

    @Test
    void whenMoreThan20Technologies_thenError() {
        // Arrange
        Capacity capacity = Capacity.builder()
                .name("Backend Dev")
                .description("Java development")
                .technologyIds(Collections.nCopies(21, 1L))
                .build();

        // Act & Assert
        StepVerifier.create(useCase.execute(capacity))
                .expectError(InvalidCapacityException.class)
                .verify();
    }

    @Test
    void whenDuplicateTechnologies_thenError() {
        // Arrange
        Capacity capacity = Capacity.builder()
                .name("Backend Dev")
                .description("Java development")
                .technologyIds(Arrays.asList(1L, 1L, 1L))
                .build();

        // Act & Assert
        StepVerifier.create(useCase.execute(capacity))
                .expectError(InvalidCapacityException.class)
                .verify();
    }

    @Test
    void whenDuplicatedName_thenError() {
        // Arrange
        Capacity capacity = Capacity.builder()
                .name("Backend Dev")
                .description("Java development")
                .technologyIds(Arrays.asList(1L, 2L, 3L))
                .build();

        when(repository.existsByName(any())).thenReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(useCase.execute(capacity))
                .expectError(DuplicatedCapacityNameException.class)
                .verify();
    }
}