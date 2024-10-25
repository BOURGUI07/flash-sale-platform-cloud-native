package com.example.aggregator_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties(prefix = "app")
public record ClientProperties(
        URI customerServiceUri,
        URI productServiceUri
) {

}
