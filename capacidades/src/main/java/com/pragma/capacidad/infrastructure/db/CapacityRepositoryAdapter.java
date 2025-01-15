package com.pragma.capacidad.infrastructure.db;

import com.pragma.capacidad.domain.model.Capacity;
import com.pragma.capacidad.domain.repository.ICapacityRepository;
import com.pragma.capacidad.infrastructure.db.entity.CapacityEntity;
import com.pragma.capacidad.infrastructure.db.entity.CapacityTechnologyEntity;
import com.pragma.capacidad.infrastructure.db.repository.CapacityCrudRepository;
import com.pragma.capacidad.infrastructure.db.repository.CapacityTechnologyCrudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class CapacityRepositoryAdapter implements ICapacityRepository {

    private final CapacityCrudRepository capacityCrud;
    private final CapacityTechnologyCrudRepository capTechCrud;

    @Override
    public Mono<Capacity> save(Capacity capacity) {
        CapacityEntity entity = new CapacityEntity();
        entity.setId(capacity.getId());
        entity.setName(capacity.getName());
        entity.setDescription(capacity.getDescription());

        return capacityCrud.save(entity)
                .flatMap(savedEntity -> {
                    Long capId = savedEntity.getId();
                    List<Long> techIds = capacity.getTechnologyIds();
                    return Flux.fromIterable(techIds)
                            .map(techId -> {
                                CapacityTechnologyEntity rel = new CapacityTechnologyEntity();
                                rel.setCapacidadId(capId);
                                rel.setTechnologyId(techId);
                                return rel;
                            })
                            .collectList()
                            .flatMap(entities -> capTechCrud.saveAll(entities).collectList())
                            .then(Mono.just(savedEntity));
                })
                .flatMap(this::joinTechnologies);
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return capacityCrud.existsByName(name);
    }

    @Override
    public Flux<Capacity> findAllOrderByNameAsc(Pageable pageable) {
        return capacityCrud.findAllOrderByNameAsc(pageable)
                .flatMap(this::joinTechnologies);
    }

    @Override
    public Flux<Capacity> findAllOrderByNameDesc(Pageable pageable) {
        return capacityCrud.findAllOrderByNameDesc(pageable)
                .flatMap(this::joinTechnologies);
    }

    @Override
    public Flux<Capacity> findAllOrderByTechCountAsc(Pageable pageable) {
        return capacityCrud.findAllOrderByTechCountAsc(pageable)
                .flatMap(this::joinTechnologies);
    }

    @Override
    public Flux<Capacity> findAllOrderByTechCountDesc(Pageable pageable) {
        return capacityCrud.findAllOrderByTechCountDesc(pageable)
                .flatMap(this::joinTechnologies);
    }

    @Override
    public Mono<Long> countCapacities() {
        return capacityCrud.countCapacities();
    }

    /**
     * Une la entity con su lista de technologyIds y retorna el dominio.
     */
    private Mono<Capacity> joinTechnologies(CapacityEntity entity) {
        return capTechCrud.findByCapacidadId(entity.getId())
                .map(CapacityTechnologyEntity::getTechnologyId)
                .collectList()
                .map(techIds ->
                        Capacity.builder()
                                .id(entity.getId())
                                .name(entity.getName())
                                .description(entity.getDescription())
                                .technologyIds(techIds)
                                .build()
                );
    }
}