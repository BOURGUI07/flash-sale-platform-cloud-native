package com.example.customer_service.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Integer orderId) {
        super("Order not found with id " + orderId);
    }
}
