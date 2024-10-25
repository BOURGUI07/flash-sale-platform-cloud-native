package com.example.aggregator_service.controller;

import com.example.aggregator_service.dto.ProductRequest;
import com.example.aggregator_service.dto.ProductResponse;
import com.example.aggregator_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;

    @GetMapping(value = "/stream/{maxPrice}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductResponse> stream(
            @PathVariable Integer maxPrice
    ){
        return service.stream(maxPrice);
    }

    @PostMapping
    public Mono<ProductResponse> createProduct(@RequestBody ProductRequest productRequest){
        return service.createProduct(productRequest);
    }
}
