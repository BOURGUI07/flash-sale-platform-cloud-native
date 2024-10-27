package com.example.customer_service.controller;

import com.example.customer_service.dto.CustomerInformation;
import com.example.customer_service.dto.CustomerRequest;
import com.example.customer_service.dto.CustomerResponse;
import com.example.customer_service.dto.PaginatedCustomerInformationResponse;
import com.example.customer_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {
    private final CustomerService service;

    @GetMapping("/{customerId}")
    public Mono<CustomerInformation> getCustomer(@PathVariable("customerId") Integer customerId) {
        log.info("Customer-Service Customer Controller:: Get Customer Information for Customer with Id: {}", customerId);
        return service.getCustomerInformation(customerId);
    }

    @GetMapping
    public Mono<PaginatedCustomerInformationResponse> getCustomers(
            @RequestParam(defaultValue = "1",required = false) int page,
            @RequestParam(defaultValue = "3",required = false) int size,
            @RequestParam(defaultValue = "balance",required = false) String sortCriteria
    ){
        log.info("Customer-Service Customer Controller:: Get Paginated Response for Customers with page: {}, size: {}, sortCriteria: {}",page,size,sortCriteria);
        return service.getCustomers(page, size, sortCriteria);
    }

    @PostMapping
    public Mono<CustomerResponse> createCustomer(@RequestBody Mono<CustomerRequest> customerRequest) {
        return customerRequest.doOnNext(req-> log.info("Customer-Service Customer Controller::Create New Customer With Request: {}",req))
                .as(service::addCustomer);
    }

    @DeleteMapping("/{customerId}")
    public Mono<Void> deleteCustomer(@PathVariable("customerId") Integer customerId) {
        log.info("Customer-Service Customer Controller:: Delete Customer with Id: {}", customerId);
        return service.deleteById(customerId);
    }
}
