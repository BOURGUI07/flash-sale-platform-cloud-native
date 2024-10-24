package com.example.product_service;

import com.example.product_service.dto.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductStreamTests extends AbstractTest {
    @Test
    void productStream(){
        client
                .get()
                .uri("/products/stream/{maxPrice}",90)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .returnResult(ProductResponse.class)
                .getResponseBody()
                .take(Duration.ofSeconds(2))
                .collectList()
                .as(StepVerifier::create)
                .assertNext(products -> {
                    assertTrue(products.stream().allMatch(x->x.currentPrice()<=90));
                })
                .verifyComplete();
    }
}
