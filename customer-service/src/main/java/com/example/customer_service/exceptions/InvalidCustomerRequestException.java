package com.example.customer_service.exceptions;

public class InvalidCustomerRequestException extends RuntimeException {
    public InvalidCustomerRequestException(String message) {
        super(message);
    }
}
