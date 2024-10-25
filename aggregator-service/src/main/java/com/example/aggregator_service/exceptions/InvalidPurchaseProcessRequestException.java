package com.example.aggregator_service.exceptions;

public class InvalidPurchaseProcessRequestException extends RuntimeException {
    public InvalidPurchaseProcessRequestException(String message) {
        super(message);
    }
}
