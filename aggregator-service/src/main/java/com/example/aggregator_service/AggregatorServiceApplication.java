package com.example.aggregator_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AggregatorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AggregatorServiceApplication.class, args);
	}

}
