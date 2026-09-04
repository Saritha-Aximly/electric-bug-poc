package com.aximly.electricbug.marker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MarkerLambdaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarkerLambdaApplication.class, args);
	}

	@Bean
	CommandLineRunner debugDbUrl(@Value("${cloud.datasource.url}") String url) {
		return args -> System.out.println(">>> ACTUAL DB URL IN USE: " + url);
	}

}
