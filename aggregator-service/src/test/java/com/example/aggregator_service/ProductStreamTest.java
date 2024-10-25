package com.example.aggregator_service;

import com.example.aggregator_service.dto.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductStreamTest extends AbstractTest {
    @Test
    void productStreamTest() {
        client
                .get()
                .uri("/products/stream/{maxPrice}",110)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .returnResult(ProductResponse.class)
                .getResponseBody()
                .take(Duration.ofSeconds(5))
                .collectList()
                .as(StepVerifier::create)
                .assertNext(products -> {
                    assertTrue(products.stream().noneMatch(x->x.currentPrice()>110));
                })
                .verifyComplete();

    }
}
