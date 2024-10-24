package com.example.customer_service.dto;

import com.example.customer_service.domain.ProductCategory;
import lombok.Builder;

@Builder
public record PurchaseRequest(
        String productCode,
        ProductCategory productCategory,
        Integer price,
        Integer quantity
) {
    public Integer totalPrice(){
        return price * quantity;
    }
}
