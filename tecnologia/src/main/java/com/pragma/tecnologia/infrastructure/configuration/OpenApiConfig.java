package com.pragma.tecnologia.infrastructure.configuration;


import com.pragma.tecnologia.infrastructure.commons.Constants;
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
                        .title(Constants.SWAGGER_TITLE)
                        .description(Constants.SWAGGER_DESCRIPTION)
                        .version(Constants.SWAGGER_VERSION)
        );
    }
}
