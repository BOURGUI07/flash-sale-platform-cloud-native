package com.example.product_service.config;

import com.example.product_service.dto.ProductResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class ApplicationConfig {
    @Bean
    public Sinks.Many<ProductResponse> productSink() {
        return Sinks.many().replay().limit(1);
    }
}
