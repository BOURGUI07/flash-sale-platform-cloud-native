package com.example.aggregator_service.exceptions;

public class ProductServiceException extends RuntimeException {
    public ProductServiceException(String failedOperation) {
        super("Failed to: " + failedOperation);
    }
}
