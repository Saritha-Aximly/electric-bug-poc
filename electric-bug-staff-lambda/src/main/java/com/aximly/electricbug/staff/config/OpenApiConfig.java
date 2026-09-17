package com.aximly.electricbug.staff.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI staffServiceOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Electric Bug — Staff Service")
                        .description("Staff records — booking reps, planners, installers")
                        .version("v1"));
    }
}