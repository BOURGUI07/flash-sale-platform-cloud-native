package com.example.customer_service.repo;

import com.example.customer_service.domain.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepo extends ReactiveCrudRepository<Customer, Integer> {
    Flux<Customer> findBy(Pageable pageable);
    Mono<Boolean> existsByNameIgnoreCase(String name);
    @Query("""
    delete from customer where id =:id
""")
    @Modifying
    Mono<Boolean> deleteByCustomerId(Integer id);
}
