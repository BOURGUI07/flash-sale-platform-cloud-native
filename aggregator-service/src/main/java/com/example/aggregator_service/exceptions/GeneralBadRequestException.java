package com.example.aggregator_service.exceptions;

public class GeneralBadRequestException extends RuntimeException {
    public GeneralBadRequestException(String message) {
        super(message);
    }
}
