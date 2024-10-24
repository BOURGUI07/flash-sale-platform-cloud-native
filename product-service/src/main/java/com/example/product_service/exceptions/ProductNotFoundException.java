package com.example.product_service.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String productCode) {
        super("Product with code " + productCode + " not found");
    }
}
