package com.pragma.capacidad.application.usecase;

import com.pragma.capacidad.application.dto.PagedResponseDto;
import com.pragma.capacidad.domain.model.Capacity;
import com.pragma.capacidad.domain.repository.ICapacityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@RequiredArgsConstructor
public class ListCapacityUseCase {

    private final ICapacityRepository repository;

    public Mono<PagedResponseDto<Capacity>> execute(String sortBy, String sortOrder, int page, int size) {
        // Validar parámetros
        if (!Arrays.asList("asc", "desc").contains(sortOrder.toLowerCase())) {
            return Mono.error(new IllegalArgumentException("sortOrder debe ser 'asc' o 'desc'"));
        }
        if (!Arrays.asList("nombre", "techCount").contains(sortBy)) {
            return Mono.error(new IllegalArgumentException("sortBy debe ser 'nombre' o 'techCount'"));
        }

        // PageRequest para la paginación
        PageRequest pageRequest = PageRequest.of(page, size);

        return repository.countCapacities()
                .flatMap(totalElements -> {
                    int totalPages = (int) Math.ceil((double) totalElements / size);

                    // Seleccionar flux en función del sortBy & sortOrder
                    Flux<Capacity> capacityFlux;
                    if ("nombre".equalsIgnoreCase(sortBy)) {
                        capacityFlux = ("asc".equalsIgnoreCase(sortOrder))
                                ? repository.findAllOrderByNameAsc(pageRequest)
                                : repository.findAllOrderByNameDesc(pageRequest);
                    } else {
                        // sortBy = "techCount"
                        capacityFlux = ("asc".equalsIgnoreCase(sortOrder))
                                ? repository.findAllOrderByTechCountAsc(pageRequest)
                                : repository.findAllOrderByTechCountDesc(pageRequest);
                    }

                    return capacityFlux
                            .collectList()
                            .map(content -> PagedResponseDto.<Capacity>builder()
                                    .content(content)
                                    .pageNumber(page)
                                    .pageSize(size)
                                    .totalElements(totalElements)
                                    .totalPages(totalPages)
                                    .isLastPage(page >= totalPages - 1)
                                    .build()
                            );
                });
    }
}