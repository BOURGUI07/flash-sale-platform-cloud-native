package com.example.product_service.validator;

import com.example.product_service.dto.ProductRequest;
import com.example.product_service.exceptions.ProductRequestExceptions;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class ProductRequestValidator {
    public static Predicate<ProductRequest> hasProductCode() {
        return dto -> Objects.nonNull(dto.productCode());
    }

    public static Predicate<ProductRequest> hasProductCategory() {
        return dto -> Objects.nonNull(dto.productCategory());
    }

    public static Predicate<ProductRequest> hasValidPrice() {
        return dto -> Objects.nonNull(dto.basePrice()) && dto.basePrice() > 0;
    }

    public static Predicate<ProductRequest> hasValidQuantity() {
        return dto -> Objects.nonNull(dto.availableQuantity()) && dto.availableQuantity() > 0;
    }

    public static UnaryOperator<Mono<ProductRequest>> validate() {
        return mono -> mono
                .filter(hasProductCode())
                .switchIfEmpty(ProductRequestExceptions.missingProductCode())
                .filter(hasProductCategory())
                .switchIfEmpty(ProductRequestExceptions.missingProductCategory())
                .filter(hasValidPrice())
                .switchIfEmpty(ProductRequestExceptions.invalidProductPrice())
                .filter(hasValidQuantity())
                .switchIfEmpty(ProductRequestExceptions.invalidProductQuantity());
    }
}
