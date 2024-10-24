package com.example.customer_service.exceptions;

public class CustomerNotFoundException extends  RuntimeException {
    public CustomerNotFoundException(Integer customerId) {
        super("Customer with id " + customerId + " not found");
    }
}
