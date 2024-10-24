package com.example.product_service.exceptions;

import reactor.core.publisher.Mono;

public class ProcessRequestExceptions {
    public static <T> Mono<T> missingProductCode(){
        return Mono.error(new InvalidPurchaseProcessRequestException("Product code is required"));
    }

    public static <T> Mono<T> invalidProductQuantity(){
        return Mono.error(new InvalidPurchaseProcessRequestException("Product desired quantity is required and should be positive"));
    }
}
