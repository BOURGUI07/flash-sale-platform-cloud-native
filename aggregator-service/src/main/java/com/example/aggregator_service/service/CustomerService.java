package com.example.aggregator_service.service;

import com.example.aggregator_service.client.CustomerServiceClient;
import com.example.aggregator_service.dto.CustomerInformation;
import com.example.aggregator_service.dto.CustomerRequest;
import com.example.aggregator_service.dto.CustomerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerServiceClient client;

    public Mono<CustomerInformation> getCustomerInfo(Integer customerId) {
        return client.getCustomerInformation(customerId);
    }

    public Mono<CustomerResponse> createCustomer(CustomerRequest request) {
        return client.createCustomer(request);
    }

    public Mono<Void> deleteCustomer(Integer customerId) {
        return client.deleteCustomer(customerId);
    }
}
