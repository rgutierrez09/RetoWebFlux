package com.pragma.capacidad.infrastructure.db;

import com.pragma.capacidad.domain.model.Capacity;
import com.pragma.capacidad.infrastructure.db.entity.CapacityEntity;
import com.pragma.capacidad.infrastructure.db.entity.CapacityTechnologyEntity;
import com.pragma.capacidad.infrastructure.db.repository.CapacityCrudRepository;
import com.pragma.capacidad.infrastructure.db.repository.CapacityTechnologyCrudRepository;
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

class CapacityRepositoryAdapterTest {

    @Mock
    private CapacityCrudRepository capacityCrud;

    @Mock
    private CapacityTechnologyCrudRepository capTechCrud;

    @InjectMocks
    private CapacityRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenSaving_thenSuccess() {
        // Arrange
        Capacity capacity = Capacity.builder()
                .name("Backend Dev")
                .description("Java development")
                .technologyIds(Arrays.asList(1L, 2L, 3L))
                .build();

        CapacityEntity savedEntity = new CapacityEntity();
        savedEntity.setId(1L);
        savedEntity.setName(capacity.getName());
        savedEntity.setDescription(capacity.getDescription());

        when(capacityCrud.save(any())).thenReturn(Mono.just(savedEntity));
        when(capTechCrud.saveAll((Iterable<CapacityTechnologyEntity>) any())).thenReturn(Flux.empty());
        when(capTechCrud.findByCapacidadId(any())).thenReturn(Flux.fromIterable(
                Arrays.asList(1L, 2L, 3L).stream().map(techId -> {
                    CapacityTechnologyEntity rel = new CapacityTechnologyEntity();
                    rel.setCapacidadId(1L);
                    rel.setTechnologyId(techId);
                    return rel;
                }).toList()
        ));

        // Act & Assert
        StepVerifier.create(adapter.save(capacity))
                .expectNextMatches(saved ->
                        saved.getId().equals(1L) &&
                                saved.getName().equals(capacity.getName()) &&
                                saved.getDescription().equals(capacity.getDescription()) &&
                                saved.getTechnologyIds().size() == 3
                )
                .verifyComplete();
    }

    @Test
    void whenCheckingExistsByName_thenSuccess() {
        // Arrange
        String name = "Backend Dev";
        when(capacityCrud.existsByName(name)).thenReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(adapter.existsByName(name))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void whenFindingAllOrderByNameAsc_thenSuccess() {
        // Arrange
        CapacityEntity entity = new CapacityEntity();
        entity.setId(1L);
        entity.setName("Backend Dev");
        entity.setDescription("Java development");

        when(capacityCrud.findAllOrderByNameAsc(any(PageRequest.class)))
                .thenReturn(Flux.just(entity));
        when(capTechCrud.findByCapacidadId(any()))
                .thenReturn(Flux.fromIterable(Arrays.asList(1L, 2L, 3L)
                        .stream()
                        .map(techId -> {
                            CapacityTechnologyEntity rel = new CapacityTechnologyEntity();
                            rel.setCapacidadId(1L);
                            rel.setTechnologyId(techId);
                            return rel;
                        })
                        .toList()));

        // Act & Assert
        StepVerifier.create(adapter.findAllOrderByNameAsc(PageRequest.of(0, 10)))
                .expectNextMatches(capacity ->
                        capacity.getId().equals(1L) &&
                                capacity.getName().equals("Backend Dev") &&
                                capacity.getTechnologyIds().size() == 3
                )
                .verifyComplete();
    }
}