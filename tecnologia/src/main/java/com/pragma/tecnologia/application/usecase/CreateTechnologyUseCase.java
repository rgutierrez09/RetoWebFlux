package com.pragma.tecnologia.application.usecase;

import com.pragma.tecnologia.domain.exception.DuplicatedTechnologyNameException;
import com.pragma.tecnologia.domain.model.Technology;
import com.pragma.tecnologia.domain.repository.ITechnologyRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class CreateTechnologyUseCase {

    private final ITechnologyRepository repository;

    public Mono<Technology> execute(Technology technology) {
        return repository.existsByName(technology.getName())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new DuplicatedTechnologyNameException(
                                "El nombre de la tecnología ya existe: " + technology.getName()));
                    }
                    return repository.save(technology);
                });
    }
}