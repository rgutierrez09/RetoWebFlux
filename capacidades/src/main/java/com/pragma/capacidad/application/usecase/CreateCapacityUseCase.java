package com.pragma.capacidad.application.usecase;

import com.pragma.capacidad.domain.exception.DuplicatedCapacityNameException;
import com.pragma.capacidad.domain.exception.InvalidCapacityException;
import com.pragma.capacidad.domain.model.Capacity;
import com.pragma.capacidad.domain.repository.ICapacityRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
public class CreateCapacityUseCase {

    private final ICapacityRepository repository;

    public Mono<Capacity> execute(Capacity capacity) {
        // Validaciones del reto
        List<Long> techIds = capacity.getTechnologyIds();
        if (techIds == null || techIds.size() < 3) {
            return Mono.error(new InvalidCapacityException("La capacidad debe tener al menos 3 tecnologías"));
        }
        if (techIds.size() > 20) {
            return Mono.error(new InvalidCapacityException("La capacidad no puede tener más de 20 tecnologías"));
        }
        // Validar que no haya IDs repetidos
        if (techIds.size() != new HashSet<>(techIds).size()) {
            return Mono.error(new InvalidCapacityException("No se permiten tecnologías repetidas en la misma capacidad"));
        }

        // Validar nombre duplicado (opcional):
        return repository.existsByName(capacity.getName())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new DuplicatedCapacityNameException(
                                "Ya existe una capacidad con el nombre: " + capacity.getName()
                        ));
                    }
                    // Guardar en BD
                    return repository.save(capacity);
                });
    }
}