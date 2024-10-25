package com.example.aggregator_service.dto;

import lombok.Builder;

@Builder
public record CancelPurchaseRequest(
        Integer orderId
) {
}
