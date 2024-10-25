package com.example.aggregator_service.exceptions;

public class InvalidCancelPurchaseRequestException extends RuntimeException {
    public InvalidCancelPurchaseRequestException(String message) {
        super(message);
    }
}
