package com.example.aggregator_service.dto;

import com.example.aggregator_service.domain.OrderStatus;
import com.example.aggregator_service.domain.ProductCategory;
import lombok.Builder;

import java.time.Instant;

@Builder
public record CustomerPurchaseResponse(
        String productCode,
        ProductCategory productCategory,
        Integer customerId,
        String customerName,
        String shippingAddress,
        Integer balance,
        Integer orderId,
        OrderStatus orderStatus,
        Integer price,
        Integer quantity,
        Integer totalPrice,
        Instant orderDate

) {
}
