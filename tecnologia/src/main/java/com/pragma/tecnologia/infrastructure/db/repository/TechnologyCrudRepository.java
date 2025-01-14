package com.pragma.tecnologia.infrastructure.db.repository;

import com.pragma.tecnologia.infrastructure.db.entity.TechnologyEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TechnologyCrudRepository extends ReactiveCrudRepository<TechnologyEntity, Long> {
    Mono<Boolean> existsByName(String name);

    @Query("SELECT * FROM tecnologias ORDER BY name ASC LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TechnologyEntity> findAllOrderedByNameAsc(Pageable pageable);

    @Query("SELECT * FROM tecnologias ORDER BY name DESC LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TechnologyEntity> findAllOrderedByNameDesc(Pageable pageable);

    @Query("SELECT COUNT(*) FROM tecnologias")
    Mono<Long> count();
}
