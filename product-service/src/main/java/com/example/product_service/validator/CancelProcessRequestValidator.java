package com.example.product_service.validator;

import com.example.product_service.dto.ProductCancelProcessRequest;
import com.example.product_service.exceptions.CancelProcessRequestExceptions;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class CancelProcessRequestValidator {

    public static Predicate<ProductCancelProcessRequest> hasCode(){
        return dto -> Objects.nonNull(dto.productCode());
    }

    public static Predicate<ProductCancelProcessRequest> hasValidQuantity(){
        return dto -> Objects.nonNull(dto.returnedQuantity()) && dto.returnedQuantity() > 0;
    }

    public static UnaryOperator<Mono<ProductCancelProcessRequest>> validate(){
        return mono -> mono
                .filter(hasCode())
                .switchIfEmpty(CancelProcessRequestExceptions.missingCode())
                .filter(hasValidQuantity())
                .switchIfEmpty(CancelProcessRequestExceptions.invalidQuantity());
    }


}
