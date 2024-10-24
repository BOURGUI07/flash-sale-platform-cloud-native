package com.example.customer_service.exceptions;

public class OrderCannotBeCancelledException extends RuntimeException {
    public OrderCannotBeCancelledException(Integer orderId) {
        super("Order with id " + orderId + " can't be cancelled");
    }
}
