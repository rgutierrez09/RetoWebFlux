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
import java.util.stream.Collectors;

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
                    List<String> techNames = capacity.getTechnologyNames();

                    return Flux.range(0, techIds.size())
                            .map(i -> {
                                CapacityTechnologyEntity rel = new CapacityTechnologyEntity();
                                rel.setCapacidadId(capId);
                                rel.setTechnologyId(techIds.get(i));
                                rel.setTechnologyName(techNames.get(i));
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

    private Mono<Capacity> joinTechnologies(CapacityEntity entity) {
        return capTechCrud.findByCapacidadId(entity.getId())
                .collectList()
                .map(techEntities -> {
                    List<Long> techIds = techEntities.stream()
                            .map(CapacityTechnologyEntity::getTechnologyId)
                            .collect(Collectors.toList());

                    List<String> techNames = techEntities.stream()
                            .map(CapacityTechnologyEntity::getTechnologyName)
                            .collect(Collectors.toList());

                    return Capacity.builder()
                            .id(entity.getId())
                            .name(entity.getName())
                            .description(entity.getDescription())
                            .technologyIds(techIds)
                            .technologyNames(techNames)
                            .build();
                });
    }
}