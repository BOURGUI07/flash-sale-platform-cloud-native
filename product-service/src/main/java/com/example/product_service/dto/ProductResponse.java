package com.example.product_service.dto;

import com.example.product_service.domain.ProductCategory;
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
