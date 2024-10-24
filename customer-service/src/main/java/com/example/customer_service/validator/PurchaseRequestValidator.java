package com.example.customer_service.validator;

import com.example.customer_service.dto.PurchaseRequest;
import com.example.customer_service.exceptions.ApplicationExceptions;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class PurchaseRequestValidator {
    public static Predicate<PurchaseRequest> hasProductCode() {
        return dto -> Objects.nonNull(dto.productCode());
    }

    public static Predicate<PurchaseRequest> hasCategory() {
        return dto -> Objects.nonNull(dto.productCategory());
    }

    public static Predicate<PurchaseRequest> hasValidQuantity() {
        return dto -> {
          var quantity = dto.quantity();
          return Objects.nonNull(quantity) && quantity > 0;
        };
    }

    public static Predicate<PurchaseRequest> hasValidPrice() {
        return dto -> {
            var price = dto.price();
            return Objects.nonNull(price) && price > 0;
        };
    }

    public static UnaryOperator<Mono<PurchaseRequest>> validate() {
        return mono -> mono
                .filter(hasProductCode())
                .switchIfEmpty(ApplicationExceptions.invalidPurchaseRequest("Product code is required"))
                .filter(hasCategory())
                .switchIfEmpty(ApplicationExceptions.invalidPurchaseRequest("Category is required"))
                .filter(hasValidQuantity())
                .switchIfEmpty(ApplicationExceptions.invalidPurchaseRequest("Quantity is required and should be positive"))
                .filter(hasValidPrice())
                .switchIfEmpty(ApplicationExceptions.invalidPurchaseRequest("Price is required and should be positive"));
    }
}
