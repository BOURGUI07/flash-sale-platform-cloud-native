package com.example.product_service.validator;

import com.example.product_service.dto.ProductPurchaseProcessRequest;
import com.example.product_service.exceptions.ProcessRequestExceptions;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class ProcessRequestValidator {
    public static Predicate<ProductPurchaseProcessRequest> hasProductCode() {
        return dto -> Objects.nonNull(dto.productCode());
    }

    public static Predicate<ProductPurchaseProcessRequest> hasValidQuantity() {
        return dto -> Objects.nonNull(dto.desiredQuantity()) && dto.desiredQuantity() > 0;
    }

    public static UnaryOperator<Mono<ProductPurchaseProcessRequest>> validate() {
        return mono -> mono
                .filter(hasProductCode())
                .switchIfEmpty(ProcessRequestExceptions.missingProductCode())
                .filter(hasValidQuantity())
                .switchIfEmpty(ProcessRequestExceptions.invalidProductQuantity());
    }
}
