package com.example.aggregator_service.dto;

import com.example.aggregator_service.domain.ProductCategory;
import lombok.Builder;

@Builder
public record PurchaseResponse(
        String productCode,
        Integer currentPrice,
        ProductCategory productCategory,
        Integer desiredQuantity
) {


}
