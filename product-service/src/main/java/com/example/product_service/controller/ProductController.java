package com.example.product_service.controller;

import com.example.product_service.dto.*;
import com.example.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @PostMapping(("/purchase-request"))
    public Mono<PurchaseResponse> getProductInformation(
            @RequestBody Mono<PurchaseRequest> request
    ){
        return service.getProductInfo(request);
    }

    @PostMapping("/process-request")
    public Mono<ProductPurchaseProcessResponse> processProductPurchase(
            @RequestBody Mono<ProductPurchaseProcessRequest> request
    ){
        return service.processPurchase(request);
    }

    @PostMapping("/process-cancel")
    public Mono<ProductCancelProcessResponse> processProductCancel(
            @RequestBody Mono<ProductCancelProcessRequest> request
    ){
        return service.processCancel(request);
    }

    @PostMapping
    public Mono<ProductResponse> createProduct(
            @RequestBody Mono<ProductRequest> request
    ){
        return service.create(request);
    }

    @GetMapping(value = "/stream/{maxPrice}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductResponse> getProductStream(
            @PathVariable Integer maxPrice
    ){
        return service.getAllProducts(maxPrice);
    }




}
