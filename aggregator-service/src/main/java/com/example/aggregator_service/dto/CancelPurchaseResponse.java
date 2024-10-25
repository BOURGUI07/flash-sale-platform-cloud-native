package com.example.aggregator_service.dto;

import com.example.aggregator_service.domain.OrderStatus;
import lombok.Builder;

import java.time.Instant;

@Builder
public record CancelPurchaseResponse(
        Integer orderId,
        String productCode,
        Integer customerId,
        Integer balance,
        Integer returnedQuantity,
        OrderStatus orderStatus,
        Instant orderDate
) {
}
