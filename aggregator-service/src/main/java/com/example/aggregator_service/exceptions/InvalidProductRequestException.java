package com.example.aggregator_service.exceptions;

public class InvalidProductRequestException extends RuntimeException {
    public InvalidProductRequestException(String message) {
        super(message);
    }
}