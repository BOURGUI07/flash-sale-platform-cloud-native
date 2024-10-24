package com.example.product_service.repo;

import com.example.product_service.domain.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepo extends ReactiveCrudRepository<Product, Integer> {
    Mono<Product> findByProductCode(String code);
    Mono<Boolean> existsByProductCode(String code);
}
