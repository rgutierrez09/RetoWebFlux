package com.pragma.tecnologia.application.usecase;


import com.pragma.tecnologia.application.dto.PagedResponseDto;
import com.pragma.tecnologia.domain.model.Technology;
import com.pragma.tecnologia.domain.repository.ITechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Arrays;

@RequiredArgsConstructor
public class ListTechnologyUseCase {
    private final ITechnologyRepository repository;

    public Mono<PagedResponseDto<Technology>> execute(String sortOrder, int page, int size) {
        if (!Arrays.asList("asc", "desc").contains(sortOrder.toLowerCase())) {
            return Mono.error(new IllegalArgumentException("Sort order must be 'asc' or 'desc'"));
        }

        PageRequest pageRequest = PageRequest.of(page, size);

        return repository.countTechnologies()
                .flatMap(total -> {
                    int totalPages = (int) Math.ceil((double) total / size);
                    Flux<Technology> technologies = "desc".equalsIgnoreCase(sortOrder) ?
                            repository.findAllOrderedByNameDesc(pageRequest) :
                            repository.findAllOrderedByNameAsc(pageRequest);

                    return technologies.collectList()
                            .map(content -> PagedResponseDto.<Technology>builder()
                                    .content(content)
                                    .pageNumber(page)
                                    .pageSize(size)
                                    .totalElements(total)
                                    .totalPages(totalPages)
                                    .isLastPage(page >= totalPages - 1)
                                    .build());
                });
    }
}