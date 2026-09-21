package com.aximly.electricbug.job.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sfn.SfnClient;

@Configuration
public class StepFunctionsConfig {

    @Bean
    public SfnClient sfnClient() {
        return SfnClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }
}