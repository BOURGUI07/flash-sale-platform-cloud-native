package com.example.product_service;

import com.example.product_service.dto.ProductPurchaseProcessRequest;
import com.example.product_service.dto.ProductPurchaseProcessResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProcessRequestTests extends AbstractTest {
    private Duration invalidProductRequest(ProductPurchaseProcessRequest productRequest, String detail) {
        return client
                .post()
                .uri("/products/process-request")
                .bodyValue(productRequest)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.BAD_REQUEST)
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problemDetail -> {
                    assertEquals("Invalid Product Purchase Process Request", problemDetail.getTitle());
                    assertEquals(detail, problemDetail.getDetail());
                })
                .verifyComplete();
    }

    @Test
    void processRequest(){
        var request = ProductPurchaseProcessRequest.builder()
                .productCode("P005")
                .desiredQuantity(2)
                .build();

        client
                .post()
                .uri("/products/process-request")
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(ProductPurchaseProcessResponse.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(response -> {
                    assertEquals("P005",response.productCode());
                    assertEquals(2, response.desiredQuantity());
                })
                .verifyComplete();
    }

    @Test
    void missingProductCode(){
        var request = ProductPurchaseProcessRequest.builder()
                .desiredQuantity(2)
                .build();

        invalidProductRequest(request,"Product code is required");
    }

    @Test
    void missingQuantity(){
        var request = ProductPurchaseProcessRequest.builder()
                .productCode("P005")
                .build();

        invalidProductRequest(request,"Product desired quantity is required and should be positive");
    }
}
