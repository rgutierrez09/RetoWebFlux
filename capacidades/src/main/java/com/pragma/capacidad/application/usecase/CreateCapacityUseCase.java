package com.pragma.capacidad.application.usecase;

import com.pragma.capacidad.domain.exception.DuplicatedCapacityNameException;
import com.pragma.capacidad.domain.exception.InvalidCapacityException;
import com.pragma.capacidad.domain.model.Capacity;
import com.pragma.capacidad.domain.repository.ICapacityRepository;
import com.pragma.capacidad.infrastructure.client.TechnologyClient;
import com.pragma.capacidad.infrastructure.commons.Constants;
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
        if (technologyNames == null || technologyNames.isEmpty()) {
            return Mono.error(new InvalidCapacityException(Constants.MINIMUM_TECHNOLOGIES));
        }

        if (technologyNames.size() < Constants.MIN_TECHNOLOGIES || technologyNames.size() > Constants.MAX_TECHNOLOGIES) {
            return Mono.error(new InvalidCapacityException(Constants.TECHNOLOGIES_RANGE));
        }

        if (technologyNames.size() != new HashSet<>(technologyNames).size()) {
            return Mono.error(new InvalidCapacityException(Constants.DUPLICATE_CAPACITY_NAME));
        }

        return Flux.fromIterable(technologyNames)
                .flatMap(technologyClient::getTechnologyByName)
                .collectList()
                .flatMap(technologies -> {
                    if (technologies.size() != technologyNames.size()) {
                        return Mono.error(new InvalidCapacityException(Constants.TECHNOLOGIES_NOT_FOUND));
                    }

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
                                            Constants.DUPLICATE_CAPACITY_NAME + capacity.getName()
                                    ));
                                }
                                return repository.save(capacity);
                            });
                });
    }
}