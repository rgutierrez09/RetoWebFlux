package com.pragma.capacidad.infrastructure.client;

import com.pragma.capacidad.application.dto.TechnologyDto;
import com.pragma.capacidad.infrastructure.commons.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TechnologyClient {

    private final WebClient webClient;

    public Mono<TechnologyDto> getTechnologyByName(String name) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(Constants.TECHNOLOGY_SEARCH_PATH)
                        .queryParam(Constants.SORT_BY_NAME, name)
                        .build())
                .retrieve()
                .bodyToMono(TechnologyDto.class);
    }
}