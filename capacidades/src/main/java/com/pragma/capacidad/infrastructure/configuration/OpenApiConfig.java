package com.pragma.capacidad.infrastructure.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CAPACIDADES")
                        .description("Microservicio para gestionar Capacidades (HU3, HU4)")
                        .version("1.0.0")
                );
    }
}