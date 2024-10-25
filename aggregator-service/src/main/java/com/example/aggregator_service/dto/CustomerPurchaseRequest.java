package com.example.aggregator_service.dto;

import com.example.aggregator_service.domain.ProductCategory;
import lombok.Builder;

@Builder
public record CustomerPurchaseRequest(
        String productCode,
        ProductCategory productCategory,
        Integer price,
        Integer quantity
) {
    public Integer totalPrice(){
        return price * quantity;
    }
}
