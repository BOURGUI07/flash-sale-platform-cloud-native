package com.example.customer_service.exceptions;

public class NotEnoughBalanceException extends RuntimeException {
    public NotEnoughBalanceException(Integer customerId) {
        super("Customer " + customerId + " does not have enough balance");
    }
}
