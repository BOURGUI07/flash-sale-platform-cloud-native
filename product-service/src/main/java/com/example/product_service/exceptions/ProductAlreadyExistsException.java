package com.example.product_service.exceptions;

public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(String productCode) {
        super("Product with code " + productCode + " already exists");
    }
}
