package com.example.product_service.exceptions;

public class InvalidCancelProcessRequestException extends RuntimeException {
    public InvalidCancelProcessRequestException(String message) {
        super(message);
    }
}
