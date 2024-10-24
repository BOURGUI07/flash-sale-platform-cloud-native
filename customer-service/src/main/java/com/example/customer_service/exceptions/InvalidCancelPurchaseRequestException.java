package com.example.customer_service.exceptions;

public class InvalidCancelPurchaseRequestException extends RuntimeException {
    public InvalidCancelPurchaseRequestException() {
        super("Order id is required");
    }
}
