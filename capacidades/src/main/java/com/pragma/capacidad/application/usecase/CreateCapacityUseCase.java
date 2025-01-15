package com.pragma.capacidad.application.usecase;

import com.pragma.capacidad.domain.exception.DuplicatedCapacityNameException;
import com.pragma.capacidad.domain.exception.InvalidCapacityException;
import com.pragma.capacidad.domain.model.Capacity;
import com.pragma.capacidad.domain.repository.ICapacityRepository;
import com.pragma.capacidad.infrastructure.client.TechnologyClient;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CreateCapacityUseCase {

    private final ICapacityRepository repository;
    private final TechnologyClient technologyClient;

    public Mono<Capacity> execute(Capacity capacity, List<String> technologyNames) {
        // Validaciones de negocio
        if (technologyNames == null || technologyNames.isEmpty()) {
            return Mono.error(new InvalidCapacityException("La capacidad debe incluir al menos 3 tecnologías"));
        }

        if (technologyNames.size() < 3 || technologyNames.size() > 20) {
            return Mono.error(new InvalidCapacityException("La capacidad debe tener entre 3 y 20 tecnologías"));
        }

        if (technologyNames.size() != new HashSet<>(technologyNames).size()) {
            return Mono.error(new InvalidCapacityException("No se permiten nombres de tecnologías repetidos"));
        }

        // Validar y obtener las tecnologías existentes
        return Flux.fromIterable(technologyNames)
                .flatMap(technologyClient::getTechnologyByName)
                .collectList()
                .flatMap(technologies -> {
                    if (technologies.size() != technologyNames.size()) {
                        return Mono.error(new InvalidCapacityException("Algunas tecnologías no existen en el sistema"));
                    }

                    // Guardamos tanto IDs como nombres
                    capacity.setTechnologyIds(technologies.stream()
                            .map(tech -> tech.getId())
                            .collect(Collectors.toList()));

                    capacity.setTechnologyNames(technologies.stream()
                            .map(tech -> tech.getName())
                            .collect(Collectors.toList()));

                    return repository.existsByName(capacity.getName())
                            .flatMap(exists -> {
                                if (exists) {
                                    return Mono.error(new DuplicatedCapacityNameException(
                                            "Ya existe una capacidad con el nombre: " + capacity.getName()
                                    ));
                                }
                                return repository.save(capacity);
                            });
                });
    }
}