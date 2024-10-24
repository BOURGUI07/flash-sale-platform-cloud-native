package com.example.product_service.dto;

import lombok.Builder;

@Builder
public record PurchaseRequest(
        String productCode,
        Integer desiredQuantity
) {
}