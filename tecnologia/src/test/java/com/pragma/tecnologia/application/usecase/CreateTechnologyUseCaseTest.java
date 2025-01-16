package com.pragma.tecnologia.application.usecase;

import com.pragma.tecnologia.domain.exception.DuplicatedTechnologyNameException;
import com.pragma.tecnologia.domain.model.Technology;
import com.pragma.tecnologia.domain.repository.ITechnologyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CreateTechnologyUseCaseTest {

    @Mock
    private ITechnologyRepository repository;

    @InjectMocks
    private CreateTechnologyUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenCreateTechnology_thenSuccess() {
        Technology technology = Technology.builder()
                .name("Java")
                .description("Programming Language")
                .build();

        when(repository.existsByName("Java")).thenReturn(Mono.just(false));
        when(repository.save(any(Technology.class))).thenReturn(Mono.just(technology));

        StepVerifier.create(useCase.execute(technology))
                .expectNext(technology)
                .verifyComplete();
    }

    @Test
    void whenCreateDuplicatedTechnology_thenError() {
        Technology technology = Technology.builder()
                .name("Java")
                .description("Programming Language")
                .build();

        when(repository.existsByName("Java")).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.execute(technology))
                .expectError(DuplicatedTechnologyNameException.class)
                .verify();
    }
}