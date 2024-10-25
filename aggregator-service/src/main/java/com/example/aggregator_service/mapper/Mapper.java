package com.example.aggregator_service.mapper;

import com.example.aggregator_service.dto.*;

public class Mapper {
    public static CustomerPurchaseRequest toCustomerPurchaseRequest(PurchaseResponse response) {
        return CustomerPurchaseRequest.builder()
                .price(response.currentPrice())
                .productCategory(response.productCategory())
                .productCode(response.productCode())
                .quantity(response.desiredQuantity())
                .build();
    }

    public static ProductPurchaseProcessRequest toPurchaseProcessRequest(CustomerPurchaseResponse response) {
        return ProductPurchaseProcessRequest.builder()
                .desiredQuantity(response.quantity())
                .productCode(response.productCode())
                .build();
    }

    public static ProductCancelProcessRequest toProductCancelProcessRequest(CancelPurchaseResponse response) {
        return ProductCancelProcessRequest.builder()
                .returnedQuantity(response.returnedQuantity())
                .productCode(response.productCode()).build();
    }
}
