package com.pragma.capacidad.infrastructure.db.repository;

import com.pragma.capacidad.infrastructure.db.entity.CapacityTechnologyEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CapacityTechnologyCrudRepository extends ReactiveCrudRepository<CapacityTechnologyEntity, Long> {

    Flux<CapacityTechnologyEntity> findByCapacidadId(Long capacidadId);

}