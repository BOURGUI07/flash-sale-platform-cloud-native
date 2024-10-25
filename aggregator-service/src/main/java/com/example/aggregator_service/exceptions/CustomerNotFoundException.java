package com.example.aggregator_service.exceptions;

public class CustomerNotFoundException extends  RuntimeException {
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
