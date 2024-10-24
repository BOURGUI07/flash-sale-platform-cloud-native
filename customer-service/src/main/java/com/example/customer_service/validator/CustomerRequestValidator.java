package com.example.customer_service.validator;

import com.example.customer_service.dto.CustomerRequest;
import com.example.customer_service.exceptions.ApplicationExceptions;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class CustomerRequestValidator {
    public static Predicate<CustomerRequest> hasName(){
        return dto -> Objects.nonNull(dto.name());
    }

    public static Predicate<CustomerRequest> hasBalance(){
        return dto -> Objects.nonNull(dto.balance());
    }

    public static Predicate<CustomerRequest> hasAddress(){
        return dto -> Objects.nonNull(dto.shippingAddress());
    }

    public static UnaryOperator<Mono<CustomerRequest>> validate(){
        return mono -> mono
                .filter(hasName())
                .switchIfEmpty(ApplicationExceptions.invalidCustomerRequest("Name is required"))
                .filter(hasBalance())
                .switchIfEmpty(ApplicationExceptions.invalidCustomerRequest("Balance is required"))
                .filter(hasAddress())
                .switchIfEmpty(ApplicationExceptions.invalidCustomerRequest("Shipping address is required"));
    }

}
