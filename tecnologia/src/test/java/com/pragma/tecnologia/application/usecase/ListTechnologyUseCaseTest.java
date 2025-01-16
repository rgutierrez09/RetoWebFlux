package com.pragma.tecnologia.application.usecase;

import com.pragma.tecnologia.domain.model.Technology;
import com.pragma.tecnologia.domain.repository.ITechnologyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ListTechnologyUseCaseTest {

    @Mock
    private ITechnologyRepository repository;

    @InjectMocks
    private ListTechnologyUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenListTechnologiesAsc_thenSuccess() {
        Technology tech1 = Technology.builder().name("Java").description("Lang").build();
        Technology tech2 = Technology.builder().name("Python").description("Lang").build();

        when(repository.countTechnologies()).thenReturn(Mono.just(2L));
        when(repository.findAllOrderedByNameAsc(any(PageRequest.class)))
                .thenReturn(Flux.just(tech1, tech2));

        StepVerifier.create(useCase.execute("asc", 0, 10))
                .expectNextMatches(response ->
                        response.getContent().size() == 2 &&
                                response.getTotalElements() == 2 &&
                                response.isLastPage())
                .verifyComplete();
    }

    @Test
    void whenInvalidSortOrder_thenError() {
        StepVerifier.create(useCase.execute("invalid", 0, 10))
                .expectError(IllegalArgumentException.class)
                .verify();
    }
}