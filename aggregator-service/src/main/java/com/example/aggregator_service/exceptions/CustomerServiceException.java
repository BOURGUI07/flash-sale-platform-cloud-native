package com.example.aggregator_service.exceptions;

public class CustomerServiceException extends RuntimeException {
    public CustomerServiceException(String failedOperation) {
        super("Failed to: " + failedOperation);
    }
}
