package com.example.customer_service.repo;

import com.example.customer_service.domain.OrderHistory;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OrderRepo extends ReactiveCrudRepository<OrderHistory, Integer> {
    Mono<OrderHistory> findByIdAndCustomerId(Integer orderId, Integer customerId);
    Flux<OrderHistory> findByCustomerId(Integer customerId);
}
