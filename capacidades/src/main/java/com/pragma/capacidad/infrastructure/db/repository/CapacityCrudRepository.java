package com.pragma.capacidad.infrastructure.db.repository;

import com.pragma.capacidad.infrastructure.db.entity.CapacityEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CapacityCrudRepository extends ReactiveCrudRepository<CapacityEntity, Long> {

    @Query("SELECT COUNT(*) FROM capacidades")
    Mono<Long> countCapacities();

    Mono<Boolean> existsByName(String name);

    @Query("""
      SELECT * 
      FROM capacidades 
      ORDER BY name ASC
      LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}
      """)
    Flux<CapacityEntity> findAllOrderByNameAsc(Pageable pageable);

    @Query("""
      SELECT * 
      FROM capacidades 
      ORDER BY name DESC
      LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}
      """)
    Flux<CapacityEntity> findAllOrderByNameDesc(Pageable pageable);

    @Query("""
      SELECT c.id, c.name, c.description
      FROM capacidades c
      LEFT JOIN capacidad_tecnologias ct ON c.id = ct.capacidad_id
      GROUP BY c.id
      ORDER BY COUNT(ct.technology_id) ASC
      LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}
      """)
    Flux<CapacityEntity> findAllOrderByTechCountAsc(Pageable pageable);

    @Query("""
      SELECT c.id, c.name, c.description
      FROM capacidades c
      LEFT JOIN capacidad_tecnologias ct ON c.id = ct.capacidad_id
      GROUP BY c.id
      ORDER BY COUNT(ct.technology_id) DESC
      LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}
      """)
    Flux<CapacityEntity> findAllOrderByTechCountDesc(Pageable pageable);
}