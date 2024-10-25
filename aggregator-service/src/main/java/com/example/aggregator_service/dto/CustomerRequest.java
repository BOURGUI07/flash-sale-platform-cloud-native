package com.example.aggregator_service.dto;

import lombok.Builder;

@Builder
public record CustomerRequest(
        String name,
        Integer balance,
        String shippingAddress
) {
}
