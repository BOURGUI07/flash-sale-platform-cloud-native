package com.example.aggregator_service.exceptions;

public class InvalidCancelProcessRequestException extends RuntimeException {
    public InvalidCancelProcessRequestException(String message) {
        super(message);
    }
}
