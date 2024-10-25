package com.example.aggregator_service.exceptions;

import reactor.core.publisher.Mono;

public class ApplicationExceptions {
    public static <T> Mono<T> customerNotFound(String message) {
        return Mono.error(new CustomerNotFoundException(message));
    }

    public static <T> Mono<T> productNotFound(String message) {
        return Mono.error(new ProductNotFoundException(message));
    }

    public static <T> Mono<T> orderNotFound(String message) {
        return Mono.error(new OrderNotFoundException(message));
    }

    public static <T> Mono<T> customerAlreadyExists(String message) {
        return Mono.error(new CustomerAlreadyExistsException(message));
    }

    public static <T> Mono<T> productAlreadyExists(String message) {
        return Mono.error(new ProductAlreadyExistsException(message));
    }

    public static <T> Mono<T> notEnoughBalanceBalance(String message) {
        return Mono.error(new NotEnoughBalanceException(message));
    }

    public static <T> Mono<T> invalidPurchaseRequest(String message) {
        return Mono.error(new InvalidPurchaseRequestException(message));
    }

    public static <T> Mono<T> invalidCustomerRequest(String message) {
        return Mono.error(new InvalidCustomerRequestException(message));
    }

    public static <T> Mono<T> invalidCancelRequest(String message) {
        return Mono.error(new InvalidCancelPurchaseRequestException(message));
    }

    public static <T> Mono<T> cannotBeCancelled(String message) {
        return Mono.error(new OrderCannotBeCancelledException(message));
    }

    public static <T> Mono<T> invalidProductRequest(String message) {
        return Mono.error(new InvalidProductRequestException(message));
    }

    public static <T> Mono<T> invalidProcessRequest(String message) {
        return Mono.error(new InvalidPurchaseProcessRequestException(message));
    }

    public static <T> Mono<T> invalidCancelProcessRequest(String message) {
        return Mono.error(new InvalidCancelProcessRequestException(message));
    }

    public static <T> Mono<T> notEnoughInventory(String message) {
        return Mono.error(new NotEnoughInventoryException(message));
    }

    public static <T> Mono<T> generalBadRequest(String message) {
        return Mono.error(new GeneralBadRequestException(message));
    }

    public static <T> Mono<T> generalNotFound(String message) {
        return Mono.error(new GeneralNotFoundException(message));
    }


}
