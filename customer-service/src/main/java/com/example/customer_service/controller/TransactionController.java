package com.example.customer_service.controller;

import com.example.customer_service.dto.CancelPurchaseRequest;
import com.example.customer_service.dto.CancelPurchaseResponse;
import com.example.customer_service.dto.PurchaseRequest;
import com.example.customer_service.dto.PurchaseResponse;
import com.example.customer_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {
    private final TransactionService service;

    @PostMapping("/{customerId}/buy")
    public Mono<PurchaseResponse> buy(
            @PathVariable Integer customerId,
            @RequestBody Mono<PurchaseRequest> request
    ){
        log.info("Customer-Service Transaction Controller::Receive Buy Request: {} with Customer Id: {}",request,customerId);
        return service.buy(customerId, request);
    }

    @PostMapping("/{customerId}/cancel")
    public Mono<CancelPurchaseResponse> cancel(
            @PathVariable Integer customerId,
            @RequestBody Mono<CancelPurchaseRequest> request
    ){
        log.info("Customer-Service Transaction Controller::Receive Cancel Request: {} with Customer Id: {}",request,customerId);
        return service.cancel(customerId, request);
    }
}
