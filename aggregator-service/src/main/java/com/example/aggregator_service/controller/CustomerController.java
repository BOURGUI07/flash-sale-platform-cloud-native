package com.example.aggregator_service.controller;

import com.example.aggregator_service.dto.CustomerInformation;
import com.example.aggregator_service.dto.CustomerRequest;
import com.example.aggregator_service.dto.CustomerResponse;
import com.example.aggregator_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
@Slf4j
public class CustomerController {
    private final CustomerService service;

    @GetMapping("/{customerId}")
    public Mono<CustomerInformation> getCustomer(@PathVariable("customerId") Integer customerId) {
        log.info("Aggregator Customer Controller::Fetching Customer Information for Customer Id: {}", customerId);
        return service.getCustomerInfo(customerId);
    }

    @PostMapping
    public Mono<CustomerResponse> createCustomer(@RequestBody CustomerRequest customerRequest) {
        log.info("Aggregator Customer Controller::Creating Customer Request: {}", customerRequest);
        return service.createCustomer(customerRequest);
    }

    @DeleteMapping("/{customerId}")
    public Mono<Void> deleteCustomer(@PathVariable("customerId") Integer customerId) {
        log.info("Aggregator Customer Controller::Deleting Customer for Customer Id: {}", customerId);
        return service.deleteCustomer(customerId);
    }
}
