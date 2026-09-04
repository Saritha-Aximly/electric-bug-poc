package com.aximly.electricbug.marker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI markerServiceOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Electric Bug — Marker Service")
                        .description("CRUD API for job installation markers")
                        .version("v1"));
    }
}