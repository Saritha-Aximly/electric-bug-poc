package com.aximly.electricbug.servicedetails.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI vehicleDetailsOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Electric Bug — Job Service Details Service")
                        .description("Service details section of a job sheet")
                        .version("v1"));
    }
}