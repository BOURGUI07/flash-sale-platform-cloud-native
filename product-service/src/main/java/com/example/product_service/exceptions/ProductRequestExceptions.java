package com.example.product_service.exceptions;

import reactor.core.publisher.Mono;

public class ProductRequestExceptions {
    public static <T> Mono<T> missingProductCode(){
        return Mono.error(new InvalidProductRequestException("Product code is required"));
    }

    public static <T> Mono<T> missingProductCategory(){
        return Mono.error(new InvalidProductRequestException("Product category is required"));
    }

    public static <T> Mono<T> invalidProductPrice(){
        return Mono.error(new InvalidProductRequestException("Product price is required and should be positive"));
    }

    public static <T> Mono<T> invalidProductQuantity(){
        return Mono.error(new InvalidProductRequestException("Product available quantity is required and should be positive"));
    }
}
