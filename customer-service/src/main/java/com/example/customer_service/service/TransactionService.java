package com.example.customer_service.service;

import com.example.customer_service.domain.Customer;
import com.example.customer_service.domain.OrderHistory;
import com.example.customer_service.domain.OrderStatus;
import com.example.customer_service.dto.CancelPurchaseRequest;
import com.example.customer_service.dto.CancelPurchaseResponse;
import com.example.customer_service.dto.PurchaseRequest;
import com.example.customer_service.dto.PurchaseResponse;
import com.example.customer_service.exceptions.ApplicationExceptions;
import com.example.customer_service.mapper.Mapper;
import com.example.customer_service.repo.CustomerRepo;
import com.example.customer_service.repo.OrderRepo;
import com.example.customer_service.validator.CancelRequestValidator;
import com.example.customer_service.validator.PurchaseRequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final CustomerRepo repo;
    private final OrderRepo orderRepo;

    @Transactional
    public Mono<PurchaseResponse> buy(Integer customerId, Mono<PurchaseRequest> request){
        return repo.findById(customerId)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId))
                .zipWhen(y -> request
                        .transform(PurchaseRequestValidator.validate())
                        .filter(req-> y.getBalance()>=req.totalPrice())
                        .switchIfEmpty(ApplicationExceptions.notEnoughBalanceBalance(customerId))
                    )
                .flatMap(x->executeBuy(x.getT1(),x.getT2()));
    }

    private Mono<PurchaseResponse> executeBuy(Customer customer, PurchaseRequest request) {
        customer.setBalance(customer.getBalance()- request.totalPrice());
        var order = OrderHistory.builder()
                .customerId(customer.getId())
                .orderStatus(OrderStatus.PENDING)
                .price(request.price())
                .orderDate(Instant.now())
                .productCategory(request.productCategory())
                .quantity(request.quantity())
                .productCode(request.productCode())
                .build();
        return Mono.zip(repo.save(customer),orderRepo.save(order))
                .map(x-> Mapper.toPurchaseResponse(x.getT1(),x.getT2()));
    }

    @Transactional
    public Mono<CancelPurchaseResponse> cancel(Integer customerId, Mono<CancelPurchaseRequest> request){
        return repo.findById(customerId)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId))
                .zipWhen(customer -> request
                        .transform(CancelRequestValidator.validate())
                        .flatMap(req ->
                            orderRepo.findByIdAndCustomerId(req.orderId(), customer.getId())
                                    .switchIfEmpty(ApplicationExceptions.orderNotFound(req.orderId()))
                                    .filter(orderHistory -> orderHistory.getOrderStatus().equals(OrderStatus.PENDING))
                                    .switchIfEmpty(ApplicationExceptions.cannotBeCancelled(req.orderId()))
                        ))
                .flatMap(x -> executeCancel(x.getT1(),x.getT2()));
    }

    private Mono<CancelPurchaseResponse> executeCancel(Customer customer, OrderHistory order) {
        customer.setBalance(customer.getBalance()+ order.getPrice()*order.getQuantity());
        order.setOrderStatus(OrderStatus.CANCELLED);
        return Mono.zip(repo.save(customer),orderRepo.save(order))
                .map(x->Mapper.toCancelPurchaseResponse(x.getT1(),x.getT2()));
    }


}
