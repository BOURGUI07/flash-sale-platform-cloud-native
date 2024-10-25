package com.example.aggregator_service.dto;

import com.example.aggregator_service.domain.OrderStatus;
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
