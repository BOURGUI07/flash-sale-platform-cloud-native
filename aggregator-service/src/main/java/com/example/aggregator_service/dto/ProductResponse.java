package com.example.aggregator_service.dto;

import com.example.aggregator_service.domain.ProductCategory;
import lombok.Builder;

@Builder
public record ProductResponse(
         Integer id,
         String productCode,
         ProductCategory productCategory,
         Integer basePrice,
         Integer currentPrice,
         Integer availableQuantity
) {
}
