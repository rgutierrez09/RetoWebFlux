package com.pragma.capacidad.domain.repository;

import com.pragma.capacidad.domain.model.Capacity;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICapacityRepository {

    Mono<Capacity> save(Capacity capacity);

    Mono<Boolean> existsByName(String name);

    Flux<Capacity> findAllOrderByNameAsc(Pageable pageable);
    Flux<Capacity> findAllOrderByNameDesc(Pageable pageable);

    Flux<Capacity> findAllOrderByTechCountAsc(Pageable pageable);
    Flux<Capacity> findAllOrderByTechCountDesc(Pageable pageable);

    Mono<Long> countCapacities();
}