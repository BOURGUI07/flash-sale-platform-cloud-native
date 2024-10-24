package com.example.customer_service.controller;

import com.example.customer_service.dto.CustomerInformation;
import com.example.customer_service.dto.CustomerRequest;
import com.example.customer_service.dto.CustomerResponse;
import com.example.customer_service.dto.PaginatedCustomerInformationResponse;
import com.example.customer_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService service;

    @GetMapping("/{customerId}")
    public Mono<CustomerInformation> getCustomer(@PathVariable("customerId") Integer customerId) {
        return service.getCustomerInformation(customerId);
    }

    @GetMapping
    public Mono<PaginatedCustomerInformationResponse> getCustomers(
            @RequestParam(defaultValue = "1",required = false) int page,
            @RequestParam(defaultValue = "3",required = false) int size,
            @RequestParam(defaultValue = "balance",required = false) String sortCriteria
    ){
        return service.getCustomers(page, size, sortCriteria);
    }

    @PostMapping
    public Mono<CustomerResponse> createCustomer(@RequestBody Mono<CustomerRequest> customerRequest) {
        return service.addCustomer(customerRequest);
    }

    @DeleteMapping("/{customerId}")
    public Mono<Void> deleteCustomer(@PathVariable("customerId") Integer customerId) {
        return service.deleteById(customerId);
    }
}
