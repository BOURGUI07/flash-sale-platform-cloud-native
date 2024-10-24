package com.example.customer_service.validator;

import com.example.customer_service.dto.CancelPurchaseRequest;
import com.example.customer_service.exceptions.ApplicationExceptions;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class CancelRequestValidator {
    public static Predicate<CancelPurchaseRequest> hasOrderId(){
        return dto -> Objects.nonNull(dto.orderId());
    }

    public static UnaryOperator<Mono<CancelPurchaseRequest>> validate(){
        return mono -> mono
                .filter(hasOrderId())
                .switchIfEmpty(ApplicationExceptions.invalidCancelRequest());
    }
}
