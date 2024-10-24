package com.example.customer_service.service;

import com.example.customer_service.dto.CustomerInformation;
import com.example.customer_service.dto.CustomerRequest;
import com.example.customer_service.dto.CustomerResponse;
import com.example.customer_service.dto.PaginatedCustomerInformationResponse;
import com.example.customer_service.exceptions.ApplicationExceptions;
import com.example.customer_service.mapper.Mapper;
import com.example.customer_service.repo.CustomerRepo;
import com.example.customer_service.repo.OrderRepo;
import com.example.customer_service.validator.CustomerRequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepo repo;
    private final OrderRepo orderRepo;

    public Mono<CustomerInformation> getCustomerInformation(Integer customerId) {
        var orderSummaries = orderRepo.findByCustomerId(customerId)
                .map(Mapper::toOrderSummary)
                .collectList();

        return repo.findById(customerId)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId))
                .zipWhen(y-> orderSummaries)
                .map(x-> Mapper.toCustomerInformation(x.getT1(),x.getT2()));
    }

    public Mono<PaginatedCustomerInformationResponse> getCustomers(int page, int size, String sortCriteria) {
        return repo.findBy(PageRequest.of(page-1, size).withSort(Sort.by(sortCriteria).descending()))
                .flatMap(customer-> getCustomerInformation(customer.getId()))
                .collectList()
                .zipWith(repo.count())
                .map(x-> PaginatedCustomerInformationResponse.builder()
                        .customerInformation(x.getT1())
                        .count(x.getT2())
                        .build());
    }

    @Transactional
    public Mono<CustomerResponse> addCustomer(Mono<CustomerRequest> request){
        return request
                .transform(CustomerRequestValidator.validate())
                .flatMap(req -> repo.existsByNameIgnoreCase(req.name())
                        .filter(b->!b)
                        .switchIfEmpty(ApplicationExceptions.customerAlreadyExists(req.name()))
                        .thenReturn(req)
                )
                .map(Mapper::toCustomer)
                .flatMap(repo::save)
                .map(Mapper::toCustomerResponse);

    }

    @Transactional
    public Mono<Void> deleteById(Integer id){
        return repo.deleteByCustomerId(id)
                .filter(b->b)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
                .then();
    }

}
