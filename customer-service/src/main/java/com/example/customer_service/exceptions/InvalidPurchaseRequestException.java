package com.example.customer_service.exceptions;

public class InvalidPurchaseRequestException extends RuntimeException {
    public InvalidPurchaseRequestException(String message) {
        super(message);
    }
}
