package com.pragma.tecnologia.application.usecase;

import com.pragma.tecnologia.domain.exception.DuplicatedTechnologyNameException;
import com.pragma.tecnologia.domain.model.Technology;
import com.pragma.tecnologia.domain.repository.ITechnologyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateTechnologyUseCaseTest {

    @Mock
    private ITechnologyRepository repository;

    private CreateTechnologyUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateTechnologyUseCase(repository);
    }

    @Test
    void execute_Success() {
        Technology technology = Technology.builder()
                .name("Java")
                .description("Programming Language")
                .build();

        Technology savedTechnology = Technology.builder()
                .id(1L)
                .name("Java")
                .description("Programming Language")
                .build();

        when(repository.existsByName("Java")).thenReturn(Mono.just(false));
        when(repository.save(technology)).thenReturn(Mono.just(savedTechnology));

        StepVerifier.create(useCase.execute(technology))
                .expectNext(savedTechnology)
                .verifyComplete();
    }

    @Test
    void execute_DuplicatedName() {
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