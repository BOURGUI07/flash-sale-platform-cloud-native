package com.example.product_service.dto;

import lombok.Builder;

@Builder
public record ProductCancelProcessResponse(
        String productCode,
        Integer returnedQuantity
) {
}
