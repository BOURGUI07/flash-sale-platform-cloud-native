package com.example.aggregator_service.dto;

import lombok.Builder;

@Builder
public record ProductCancelProcessRequest(
        String productCode,
        Integer returnedQuantity
) {
}
