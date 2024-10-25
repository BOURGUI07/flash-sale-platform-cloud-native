package com.example.aggregator_service.controller;

import com.example.aggregator_service.dto.CancelPurchaseRequest;
import com.example.aggregator_service.dto.CancelPurchaseResponse;
import com.example.aggregator_service.dto.CustomerPurchaseResponse;
import com.example.aggregator_service.dto.PurchaseRequest;
import com.example.aggregator_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService service;

    @PostMapping("/{customerId}/buy")
    public Mono<CustomerPurchaseResponse> buy(
            @PathVariable("customerId") Integer customerId,
            @RequestBody PurchaseRequest purchaseRequest) {
        return service.buy(customerId,purchaseRequest);
    }

    @PostMapping("/{customerId}/cancel")
    public Mono<CancelPurchaseResponse> cancel(
            @PathVariable("customerId") Integer customerId,
            @RequestBody CancelPurchaseRequest purchaseRequest) {
        return service.cancel(customerId,purchaseRequest);
    }


}
