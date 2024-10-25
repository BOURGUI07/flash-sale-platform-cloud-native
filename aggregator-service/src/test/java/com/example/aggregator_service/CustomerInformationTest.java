package com.example.aggregator_service;

import com.example.aggregator_service.dto.CustomerInformation;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomerInformationTest extends AbstractTest {
    private WebTestClient.ResponseSpec getCustomerInformation(Integer id, HttpStatus expectedStatus) {
        return client
                .get()
                .uri("/customers/{id}",id)
                .exchange()
                .expectStatus()
                .isEqualTo(expectedStatus);
    }

    @Test
    void validCustomerInformation() throws JsonProcessingException {
        getCustomerInformation(1, HttpStatus.OK)
                .returnResult(CustomerInformation.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(response -> {
                    assertEquals("Alice", response.name());
                    assertEquals(5000,response.balance());
                    assertTrue(response.orderSummaries().isEmpty());
                })
                .verifyComplete();
    }

    @Test
    void notFoundCustomerInformation() {
        getCustomerInformation(99, HttpStatus.NOT_FOUND)
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(response -> {
                    assertEquals("Customer Not Found", response.getTitle());
                    assertEquals("Customer with id 99 not found", response.getDetail());
                })
                .verifyComplete();
    }
}
