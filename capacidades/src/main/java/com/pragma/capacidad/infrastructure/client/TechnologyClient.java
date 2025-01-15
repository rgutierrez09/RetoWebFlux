package com.pragma.capacidad.infrastructure.client;

import com.pragma.capacidad.application.dto.TechnologyDto;
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
                        .path("/webflux/v1/tecnologias/buscar")
                        .queryParam("nombre", name)
                        .build())
                .retrieve()
                .bodyToMono(TechnologyDto.class);
    }
}