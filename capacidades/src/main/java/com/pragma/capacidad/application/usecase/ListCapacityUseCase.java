package com.pragma.capacidad.application.usecase;

import com.pragma.capacidad.application.dto.PagedResponseDto;
import com.pragma.capacidad.domain.model.Capacity;
import com.pragma.capacidad.domain.repository.ICapacityRepository;
import com.pragma.capacidad.infrastructure.commons.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@RequiredArgsConstructor
public class ListCapacityUseCase {

    private final ICapacityRepository repository;

    public Mono<PagedResponseDto<Capacity>> execute(String sortBy, String sortOrder, int page, int size) {
        if (!Arrays.asList(Constants.DEFAULT_SORT_ASC, Constants.SORT_ORDER_DESC).contains(sortOrder.toLowerCase())) {
            return Mono.error(new IllegalArgumentException(Constants.INVALID_SORT_ORDER));
        }
        if (!Arrays.asList(Constants.NAME, Constants.SORT_BY_TECH_COUNT).contains(sortBy)) {
            return Mono.error(new IllegalArgumentException(Constants.INVALID_SORT_BY));
        }

        PageRequest pageRequest = PageRequest.of(page, size);

        return repository.countCapacities()
                .flatMap(totalElements -> {
                    int totalPages = (int) Math.ceil((double) totalElements / size);

                    Flux<Capacity> capacityFlux;
                    if (Constants.NAME.equalsIgnoreCase(sortBy)) {
                        capacityFlux = (Constants.DEFAULT_SORT_ASC.equalsIgnoreCase(sortOrder))
                                ? repository.findAllOrderByNameAsc(pageRequest)
                                : repository.findAllOrderByNameDesc(pageRequest);
                    } else {
                        capacityFlux = (Constants.DEFAULT_SORT_ASC.equalsIgnoreCase(sortOrder))
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