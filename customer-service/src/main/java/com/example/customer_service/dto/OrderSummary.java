package com.example.customer_service.dto;

import com.example.customer_service.domain.OrderStatus;
import lombok.Builder;

import java.time.Instant;

@Builder
public record OrderSummary(
        String productCode,
        Integer price,
        Integer quantity,
        OrderStatus status,
        Instant orderDate
) {
}
