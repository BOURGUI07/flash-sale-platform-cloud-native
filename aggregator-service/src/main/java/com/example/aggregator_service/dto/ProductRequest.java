package com.example.aggregator_service.dto;

import com.example.aggregator_service.domain.ProductCategory;
import lombok.Builder;

@Builder
public record ProductRequest(
         ProductCategory productCategory,
         String productCode,
         Integer basePrice,
         Integer availableQuantity
) {
}
