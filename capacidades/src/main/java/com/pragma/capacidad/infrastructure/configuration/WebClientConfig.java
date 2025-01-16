package com.pragma.capacidad.infrastructure.configuration;

import com.pragma.capacidad.infrastructure.commons.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                .baseUrl(Constants.BASE_URL_TECHNOLOGIES)
                .build();
    }
}