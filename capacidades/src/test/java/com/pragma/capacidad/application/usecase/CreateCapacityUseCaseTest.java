package com.pragma.capacidad.application.usecase;

import com.pragma.capacidad.application.dto.TechnologyDto;
import com.pragma.capacidad.domain.exception.DuplicatedCapacityNameException;
import com.pragma.capacidad.domain.exception.InvalidCapacityException;
import com.pragma.capacidad.domain.model.Capacity;
import com.pragma.capacidad.domain.repository.ICapacityRepository;
import com.pragma.capacidad.infrastructure.client.TechnologyClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCapacityUseCaseTest {

    @Mock
    private ICapacityRepository repository;

    @Mock
    private TechnologyClient technologyClient;

    @InjectMocks
    private CreateCapacityUseCase useCase;

    private Capacity capacity;
    private List<String> validTechnologyNames;
    private List<TechnologyDto> validTechnologies;

    @BeforeEach
    void setUp() {
        capacity = Capacity.builder()
                .name("Backend Java")
                .description("Desarrollo backend con Java y Spring")
                .build();

        validTechnologyNames = Arrays.asList("Java", "Spring", "PostgreSQL");
        validTechnologies = Arrays.asList(
                createTechnologyDto(1L, "Java"),
                createTechnologyDto(2L, "Spring"),
                createTechnologyDto(3L, "PostgreSQL")
        );
    }

    @Test
    void whenCreateValidCapacity_thenSuccess() {
        // Arrange
        when(repository.existsByName(any())).thenReturn(Mono.just(false));
        when(repository.save(any(Capacity.class))).thenReturn(Mono.just(capacity));

        for (int i = 0; i < validTechnologyNames.size(); i++) {
            when(technologyClient.getTechnologyByName(validTechnologyNames.get(i)))
                    .thenReturn(Mono.just(validTechnologies.get(i)));
        }

        // Act & Assert
        StepVerifier.create(useCase.execute(capacity, validTechnologyNames))
                .expectNextMatches(saved ->
                        saved.getName().equals(capacity.getName()) &&
                                saved.getDescription().equals(capacity.getDescription()) &&
                                saved.getTechnologyNames().containsAll(validTechnologyNames)
                )
                .verifyComplete();
    }

    @Test
    void whenNoTechnologies_thenThrowInvalidCapacityException() {
        // Act & Assert
        StepVerifier.create(useCase.execute(capacity, Collections.emptyList()))
                .expectError(InvalidCapacityException.class)
                .verify();

        verify(technologyClient, never()).getTechnologyByName(any());
        verify(repository, never()).save(any());
    }

    @Test
    void whenTooFewTechnologies_thenThrowInvalidCapacityException() {
        // Arrange
        List<String> tooFewTechs = Arrays.asList("Java", "Spring");

        // Act & Assert
        StepVerifier.create(useCase.execute(capacity, tooFewTechs))
                .expectError(InvalidCapacityException.class)
                .verify();

        verify(technologyClient, never()).getTechnologyByName(any());
        verify(repository, never()).save(any());
    }

    @Test
    void whenTooManyTechnologies_thenThrowInvalidCapacityException() {
        // Arrange
        List<String> tooManyTechs = Arrays.asList(
                "Tech1", "Tech2", "Tech3", "Tech4", "Tech5",
                "Tech6", "Tech7", "Tech8", "Tech9", "Tech10",
                "Tech11", "Tech12", "Tech13", "Tech14", "Tech15",
                "Tech16", "Tech17", "Tech18", "Tech19", "Tech20", "Tech21"
        );

        // Act & Assert
        StepVerifier.create(useCase.execute(capacity, tooManyTechs))
                .expectError(InvalidCapacityException.class)
                .verify();

        verify(technologyClient, never()).getTechnologyByName(any());
        verify(repository, never()).save(any());
    }

    @Test
    void whenDuplicateTechnologyNames_thenThrowInvalidCapacityException() {
        // Arrange
        List<String> duplicateTechs = Arrays.asList("Java", "Spring", "Java");

        // Act & Assert
        StepVerifier.create(useCase.execute(capacity, duplicateTechs))
                .expectError(InvalidCapacityException.class)
                .verify();

        verify(technologyClient, never()).getTechnologyByName(any());
        verify(repository, never()).save(any());
    }

    @Test
    void whenTechnologyNotFound_thenThrowInvalidCapacityException() {
        // Arrange
        List<String> techsWithNonExistent = Arrays.asList("Java", "Spring", "NonExistentTech");

        when(technologyClient.getTechnologyByName("Java"))
                .thenReturn(Mono.just(createTechnologyDto(1L, "Java")));
        when(technologyClient.getTechnologyByName("Spring"))
                .thenReturn(Mono.just(createTechnologyDto(2L, "Spring")));
        when(technologyClient.getTechnologyByName("NonExistentTech"))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(useCase.execute(capacity, techsWithNonExistent))
                .expectError(InvalidCapacityException.class)
                .verify();

        verify(repository, never()).save(any());
    }

    @Test
    void whenCapacityNameExists_thenThrowDuplicatedException() {
        // Arrange
        when(repository.existsByName(any())).thenReturn(Mono.just(true));

        for (String techName : validTechnologyNames) {
            when(technologyClient.getTechnologyByName(techName))
                    .thenReturn(Mono.just(createTechnologyDto(1L, techName)));
        }

        // Act & Assert
        StepVerifier.create(useCase.execute(capacity, validTechnologyNames))
                .expectError(DuplicatedCapacityNameException.class)
                .verify();

        verify(repository, never()).save(any());
    }

    private TechnologyDto createTechnologyDto(Long id, String name) {
        TechnologyDto dto = new TechnologyDto();
        dto.setId(id);
        dto.setName(name);
        return dto;
    }
}