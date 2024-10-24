package com.example.product_service.exceptions;

import reactor.core.publisher.Mono;

public class ApplicationExceptions {
    public static <T> Mono<T> productNotFound(String productCode) {
        return Mono.error(new ProductNotFoundException(productCode));
    }

    public static <T> Mono<T> notEnoughInventory(String productCode) {
        return Mono.error(new NotEnoughInventoryException(productCode));
    }

    public static <T> Mono<T> productAlreadyExists(String productCode) {
        return Mono.error(new ProductAlreadyExistsException(productCode));
    }
}
