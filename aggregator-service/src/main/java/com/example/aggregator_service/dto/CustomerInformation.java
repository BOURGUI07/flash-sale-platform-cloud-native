package com.example.aggregator_service.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CustomerInformation(
        Integer customerId,
        String name,
        Integer balance,
        String shippingAddress,
        List<OrderSummary> orderSummaries
) {
}
