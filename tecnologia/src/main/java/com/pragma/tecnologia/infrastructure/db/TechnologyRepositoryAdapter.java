package com.pragma.tecnologia.infrastructure.db;

import com.pragma.tecnologia.domain.model.Technology;
import com.pragma.tecnologia.domain.repository.ITechnologyRepository;
import com.pragma.tecnologia.infrastructure.db.entity.TechnologyEntity;
import com.pragma.tecnologia.infrastructure.db.repository.TechnologyCrudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TechnologyRepositoryAdapter implements ITechnologyRepository {
    private final TechnologyCrudRepository repository;

    @Override
    public Mono<Technology> save(Technology technology) {
        TechnologyEntity entity = new TechnologyEntity();
        entity.setId(technology.getId());
        entity.setName(technology.getName());
        entity.setDescription(technology.getDescription());

        return repository.save(entity)
                .map(this::toTechnologyDomain);
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public Flux<Technology> findAllOrderedByNameAsc(Pageable pageable) {
        return repository.findAllOrderedByNameAsc(pageable)
                .map(this::toTechnologyDomain);
    }

    @Override
    public Flux<Technology> findAllOrderedByNameDesc(Pageable pageable) {
        return repository.findAllOrderedByNameDesc(pageable)
                .map(this::toTechnologyDomain);
    }

    @Override
    public Mono<Long> countTechnologies() {
        return repository.count();
    }

    private Technology toTechnologyDomain(TechnologyEntity entity) {
        return Technology.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }
}