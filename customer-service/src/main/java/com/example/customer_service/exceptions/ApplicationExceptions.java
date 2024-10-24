package com.example.customer_service.exceptions;

import reactor.core.publisher.Mono;

public class ApplicationExceptions {
    public static <T> Mono<T> customerNotFound(Integer customerId) {
        return Mono.error(new CustomerNotFoundException(customerId));
    }

    public static <T> Mono<T> orderNotFound(Integer orderId) {
        return Mono.error(new OrderNotFoundException(orderId));
    }

    public static <T> Mono<T> customerAlreadyExists(String customerName) {
        return Mono.error(new CustomerAlreadyExistsException(customerName));
    }

    public static <T> Mono<T> notEnoughBalanceBalance(Integer customerId) {
        return Mono.error(new NotEnoughBalanceException(customerId));
    }

    public static <T> Mono<T> invalidPurchaseRequest(String message) {
        return Mono.error(new InvalidPurchaseRequestException(message));
    }

    public static <T> Mono<T> invalidCustomerRequest(String message) {
        return Mono.error(new InvalidCustomerRequestException(message));
    }

    public static <T> Mono<T> invalidCancelRequest() {
        return Mono.error(new InvalidCancelPurchaseRequestException());
    }

    public static <T> Mono<T> cannotBeCancelled(Integer orderId) {
        return Mono.error(new OrderCannotBeCancelledException(orderId));
    }
}
