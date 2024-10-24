package com.example.customer_service.dto;

import lombok.Builder;

@Builder
public record CustomerResponse(
        Integer customerId,
        String name,
        Integer balance,
        String shippingAddress
) {
}
