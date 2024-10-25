package com.example.aggregator_service.service;

import com.example.aggregator_service.client.ProductServiceClient;
import com.example.aggregator_service.dto.ProductRequest;
import com.example.aggregator_service.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductServiceClient client;

    public Mono<ProductResponse> createProduct(ProductRequest productRequest) {
        return client.createProduct(productRequest);
    }

    public Flux<ProductResponse> stream(Integer maxPrice){
        return client.productResponseFlux(maxPrice);
    }
}
