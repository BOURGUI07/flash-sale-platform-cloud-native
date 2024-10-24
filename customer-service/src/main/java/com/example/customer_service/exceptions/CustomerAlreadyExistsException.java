package com.example.customer_service.exceptions;

public class CustomerAlreadyExistsException extends RuntimeException {
    public CustomerAlreadyExistsException(String customerName) {
        super("Customer " + customerName + " already exists");
    }
}
