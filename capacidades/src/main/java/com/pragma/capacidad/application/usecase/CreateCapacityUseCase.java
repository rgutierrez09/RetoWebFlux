package com.pragma.capacidad.application.usecase;

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
        // Validaciones: nombre, descripción ya vienen validadas por DTO (@NotBlank, etc.)
        // Validar min 3, max 20
        List<Long> techs = capacity.getTechnologyIds();
        if (techs == null || techs.size() < 3) {
            return Mono.error(new InvalidCapacityException("Debe tener al menos 3 tecnologías"));
        }
        if (techs.size() > 20) {
            return Mono.error(new InvalidCapacityException("No puede tener más de 20 tecnologías"));
        }
        // Validar que no haya IDs repetidos
        if (techs.size() != new HashSet<>(techs).size()) {
            return Mono.error(new InvalidCapacityException("No se permiten tecnologías repetidas en la misma capacidad"));
        }

        // Si no validamos nombre duplicado:
        return repository.save(capacity);
    }
}