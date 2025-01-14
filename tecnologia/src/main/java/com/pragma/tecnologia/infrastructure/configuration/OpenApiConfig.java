package com.pragma.tecnologia.infrastructure.configuration;


import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(
                new Info()
                        .title("TECNOLOGIAS")
                        .description("Microservicio para gestionar Tecnologias del reto de Reactivo")
                        .version("1.0.0")
        );
    }
}
