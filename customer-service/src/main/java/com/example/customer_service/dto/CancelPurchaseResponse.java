package com.example.customer_service.dto;

import com.example.customer_service.domain.OrderStatus;
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
