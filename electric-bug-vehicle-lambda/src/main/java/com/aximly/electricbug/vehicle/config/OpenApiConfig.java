package com.aximly.electricbug.vehicle.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI vehicleServiceOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Electric Bug — Vehicle Service")
                        .description("Vehicle makes and models reference data")
                        .version("v1"));
    }
}