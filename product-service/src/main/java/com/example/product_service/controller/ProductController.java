package com.example.product_service.controller;

import com.example.product_service.dto.*;
import com.example.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService service;

    @PostMapping(("/purchase-request"))
    public Mono<PurchaseResponse> getProductInformation(
            @RequestBody Mono<PurchaseRequest> request
    ){
        return request.doOnNext(req -> log.info("Product-Service Product Controller::Get Product Info with Request: {}",req))
                .as(service::getProductInfo);
    }

    @PostMapping("/process-request")
    public Mono<ProductPurchaseProcessResponse> processProductPurchase(
            @RequestBody Mono<ProductPurchaseProcessRequest> request
    ){
        return request.doOnNext(req -> log.info("Product-Service Product Controller::Process Purchase Request: {}",req))
                .as(service::processPurchase);
    }

    @PostMapping("/process-cancel")
    public Mono<ProductCancelProcessResponse> processProductCancel(
            @RequestBody Mono<ProductCancelProcessRequest> request
    ){
        return request.doOnNext(req -> log.info("Product-Service Product Controller::Process Cancel Request: {}",req))
                .as(service::processCancel);
    }

    @PostMapping
    public Mono<ProductResponse> createProduct(
            @RequestBody Mono<ProductRequest> request
    ){
        return request.doOnNext(req -> log.info("Product-Service Product Controller::Create New Product With Request: {}",req))
                .as(service::create);
    }

    @GetMapping(value = "/stream/{maxPrice}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductResponse> getProductStream(
            @PathVariable Integer maxPrice
    ){
        log.info("Product-Service Product Controller::Get Product Stream with Current Prices Lower than: {}",maxPrice);
        return service.getAllProducts(maxPrice);
    }




}
