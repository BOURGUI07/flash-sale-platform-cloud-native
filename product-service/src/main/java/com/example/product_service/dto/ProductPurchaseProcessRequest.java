package com.example.product_service.dto;

import lombok.Builder;

@Builder
public record ProductPurchaseProcessRequest (
        String productCode,
        Integer desiredQuantity
){
}
