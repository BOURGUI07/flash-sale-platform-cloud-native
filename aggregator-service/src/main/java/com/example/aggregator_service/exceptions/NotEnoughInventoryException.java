package com.example.aggregator_service.exceptions;

public class NotEnoughInventoryException extends RuntimeException {
    public NotEnoughInventoryException(String productCode) {
        super("Product " + productCode + " does not have enough inventory");
    }
}
