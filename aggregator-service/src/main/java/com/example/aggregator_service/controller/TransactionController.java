package com.example.aggregator_service.controller;

import com.example.aggregator_service.dto.CancelPurchaseRequest;
import com.example.aggregator_service.dto.CancelPurchaseResponse;
import com.example.aggregator_service.dto.CustomerPurchaseResponse;
import com.example.aggregator_service.dto.PurchaseRequest;
import com.example.aggregator_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
@Slf4j
public class TransactionController {
    private final TransactionService service;

    @PostMapping("/{customerId}/buy")
    public Mono<CustomerPurchaseResponse> buy(
            @PathVariable("customerId") Integer customerId,
            @RequestBody PurchaseRequest purchaseRequest) {
        log.info("Aggregator Transaction Controller:: Receiving a Buy Request: {}, by Customer with Id: {}", purchaseRequest, customerId);
        return service.buy(customerId,purchaseRequest);
    }

    @PostMapping("/{customerId}/cancel")
    public Mono<CancelPurchaseResponse> cancel(
            @PathVariable("customerId") Integer customerId,
            @RequestBody CancelPurchaseRequest purchaseRequest) {
        log.info("Aggregator Transaction Controller:: Receiving a Cancel Request: {}, by Customer with Id: {}", purchaseRequest, customerId);
        return service.cancel(customerId,purchaseRequest);
    }


}
