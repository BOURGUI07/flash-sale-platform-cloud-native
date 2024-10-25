package com.example.aggregator_service.config;

import com.example.aggregator_service.client.CustomerServiceClient;
import com.example.aggregator_service.client.ProductServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ServiceClientConfig {
    private WebClient createWebClient(String baseUrl) {
        return WebClient.builder().baseUrl(baseUrl).build();
    }

    @Bean
    public CustomerServiceClient customerServiceClient(
            ClientProperties clientProperties){
        return new CustomerServiceClient(createWebClient(clientProperties.customerServiceUri().toString()));
    }

    @Bean
    public ProductServiceClient productServiceClient(
            ClientProperties clientProperties){
        return new ProductServiceClient(createWebClient(clientProperties.productServiceUri().toString()));
    }



}
