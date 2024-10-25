package com.example.aggregator_service.controller;

import com.example.aggregator_service.dto.CustomerInformation;
import com.example.aggregator_service.dto.CustomerRequest;
import com.example.aggregator_service.dto.CustomerResponse;
import com.example.aggregator_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService service;

    @GetMapping("/{customerId}")
    public Mono<CustomerInformation> getCustomer(@PathVariable("customerId") Integer customerId) {
        return service.getCustomerInfo(customerId);
    }

    @PostMapping
    public Mono<CustomerResponse> createCustomer(@RequestBody CustomerRequest customerRequest) {
        return service.createCustomer(customerRequest);
    }

    @DeleteMapping("/{customerId}")
    public Mono<Void> deleteCustomer(@PathVariable("customerId") Integer customerId) {
        return service.deleteCustomer(customerId);
    }
}
