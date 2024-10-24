package com.example.product_service.exceptions;

import reactor.core.publisher.Mono;

public class CancelProcessRequestExceptions {
    public static <T> Mono<T> missingCode(){
        return Mono.error(new InvalidCancelProcessRequestException("Product code is required"));
    }

    public static <T> Mono<T> invalidQuantity(){
        return Mono.error(new InvalidCancelProcessRequestException("Product quantity is required and should be positive"));
    }
}
