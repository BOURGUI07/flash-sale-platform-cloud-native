package com.example.aggregator_service.exceptions;

public class ExternalServiceTimeoutException extends RuntimeException {
    public ExternalServiceTimeoutException(String serviceName) {
        super("Timeout Expired. Couldn't Connect to: " + serviceName);
    }
}
