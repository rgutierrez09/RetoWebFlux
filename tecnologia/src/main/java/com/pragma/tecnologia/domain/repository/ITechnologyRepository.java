package com.pragma.tecnologia.domain.repository;

import com.pragma.tecnologia.domain.model.Technology;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITechnologyRepository {
    Mono<Technology> save(Technology technology);
    Mono<Boolean> existsByName(String name);
    Flux<Technology> findAllOrderedByNameAsc(Pageable pageable);
    Flux<Technology> findAllOrderedByNameDesc(Pageable pageable);
    Mono<Long> countTechnologies();
}
