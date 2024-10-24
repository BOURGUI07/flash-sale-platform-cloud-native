package com.example.product_service.validator;

import com.example.product_service.dto.PurchaseRequest;
import com.example.product_service.exceptions.PurchaseRequestExceptions;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class PurchaseRequestValidator {
    public static Predicate<PurchaseRequest> hasProductCode() {
        return dto -> Objects.nonNull(dto.productCode());
    }

    public static Predicate<PurchaseRequest> hasValidQuantity() {
        return dto -> Objects.nonNull(dto.desiredQuantity()) && dto.desiredQuantity() > 0;
    }

    public static UnaryOperator<Mono<PurchaseRequest>> validate() {
        return mono -> mono
                .filter(hasProductCode())
                .switchIfEmpty(PurchaseRequestExceptions.missingProductCode())
                .filter(hasValidQuantity())
                .switchIfEmpty(PurchaseRequestExceptions.invalidProductQuantity());
    }
}
