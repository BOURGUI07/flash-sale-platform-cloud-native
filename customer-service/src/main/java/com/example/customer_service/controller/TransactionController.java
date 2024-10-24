package com.example.customer_service.controller;

import com.example.customer_service.dto.CancelPurchaseRequest;
import com.example.customer_service.dto.CancelPurchaseResponse;
import com.example.customer_service.dto.PurchaseRequest;
import com.example.customer_service.dto.PurchaseResponse;
import com.example.customer_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService service;

    @PostMapping("/{customerId}/buy")
    public Mono<PurchaseResponse> buy(
            @PathVariable Integer customerId,
            @RequestBody Mono<PurchaseRequest> request
    ){
        return service.buy(customerId, request);
    }

    @PostMapping("/{customerId}/cancel")
    public Mono<CancelPurchaseResponse> cancel(
            @PathVariable Integer customerId,
            @RequestBody Mono<CancelPurchaseRequest> request
    ){
        return service.cancel(customerId, request);
    }
}
