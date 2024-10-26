package com.example.aggregator_service.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "apps")
public record AppProperties(
        String greeting
) {

}
