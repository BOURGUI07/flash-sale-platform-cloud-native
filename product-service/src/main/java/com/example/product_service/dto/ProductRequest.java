package com.example.product_service.dto;

import com.example.product_service.domain.ProductCategory;
import lombok.Builder;

@Builder
public record ProductRequest(
         ProductCategory productCategory,
         String productCode,
         Integer basePrice,
         Integer availableQuantity
) {
}
