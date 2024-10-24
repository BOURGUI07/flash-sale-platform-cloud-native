package com.example.product_service.mapper;

import com.example.product_service.domain.Product;
import com.example.product_service.dto.*;

public class Mapper {
    public static Product toProduct(ProductRequest productRequest) {
        return Product.builder()
                .productCode(productRequest.productCode())
                .availableQuantity(productRequest.availableQuantity())
                .basePrice(productRequest.basePrice())
                .productCategory(productRequest.productCategory())
                .currentPrice(productRequest.basePrice())
                .build();
    }

    public static ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .productCode(product.getProductCode())
                .availableQuantity(product.getAvailableQuantity())
                .basePrice(product.getBasePrice())
                .productCategory(product.getProductCategory())
                .currentPrice(product.getCurrentPrice())
                .id(product.getId())
                .build();
    }

    public static PurchaseResponse toPurchaseResponse(Product product, PurchaseRequest request) {
        return PurchaseResponse.builder()
                .productCode(product.getProductCode())
                .currentPrice(product.getCurrentPrice())
                .productCategory(product.getProductCategory())
                .desiredQuantity(request.desiredQuantity())
                .build();

    }

    public static ProductPurchaseProcessResponse toProductPurchaseProcessResponse(ProductPurchaseProcessRequest request) {
        return ProductPurchaseProcessResponse.builder()
                .productCode(request.productCode())
                .desiredQuantity(request.desiredQuantity())
                .build();
    }

    public static ProductCancelProcessResponse toProductCancelProcessResponse(ProductCancelProcessRequest request) {
        return ProductCancelProcessResponse.builder()
                .productCode(request.productCode())
                .returnedQuantity(request.returnedQuantity())
                .build();
    }
}
