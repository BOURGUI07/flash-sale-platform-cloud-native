package com.example.aggregator_service.service;

import com.example.aggregator_service.client.CustomerServiceClient;
import com.example.aggregator_service.client.ProductServiceClient;
import com.example.aggregator_service.dto.CancelPurchaseRequest;
import com.example.aggregator_service.dto.CancelPurchaseResponse;
import com.example.aggregator_service.dto.CustomerPurchaseResponse;
import com.example.aggregator_service.dto.PurchaseRequest;
import com.example.aggregator_service.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final ProductServiceClient productClient;
    private final CustomerServiceClient customerClient;

    public Mono<CustomerPurchaseResponse> buy(Integer customerId, PurchaseRequest request) {
        return productClient.getProductInfo(request)
                .map(Mapper::toCustomerPurchaseRequest)
                .flatMap(x->customerClient.buy(customerId,x))
                .flatMap(y ->productClient.processPurchase(Mapper.toPurchaseProcessRequest(y))
                        .thenReturn(y));
    }

    public Mono<CancelPurchaseResponse> cancel(Integer customerId, CancelPurchaseRequest request) {
        return customerClient.cancel(customerId,request)
                .flatMap(response -> productClient.processCancel(Mapper.toProductCancelProcessRequest(response))
                        .thenReturn(response));

    }
}
