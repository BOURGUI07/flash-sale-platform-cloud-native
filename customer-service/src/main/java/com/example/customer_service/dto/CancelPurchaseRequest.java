package com.example.customer_service.dto;

import lombok.Builder;

@Builder
public record CancelPurchaseRequest(
        Integer orderId
) {
}
