package com.example.aggregator_service.dto;

import lombok.Builder;

@Builder
public record ProductPurchaseProcessResponse(
        String productCode,
        Integer desiredQuantity
) {
}
