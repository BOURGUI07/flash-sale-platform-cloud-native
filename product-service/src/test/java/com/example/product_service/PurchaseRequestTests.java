package com.example.product_service;

import com.example.product_service.domain.ProductCategory;
import com.example.product_service.dto.PurchaseRequest;
import com.example.product_service.dto.PurchaseResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PurchaseRequestTests extends AbstractTest {
    public WebTestClient.ResponseSpec findByCodeValid(PurchaseRequest request, HttpStatus expectedStatus) {
        return client
                .post()
                .uri("/products/purchase-request")
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isEqualTo(expectedStatus);
    }

    public Duration findByCodeInvalid(PurchaseRequest request, HttpStatus expectedStatus, String problemTitle) {
        return client
                .post()
                .uri("/products/purchase-request")
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isEqualTo(expectedStatus)
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(response -> {
                    assertEquals(problemTitle,response.getTitle());
                })
                .verifyComplete();
    }

    @Test
    void testValidPurchaseRequest() {
        var purchaseRequest = PurchaseRequest.builder()
                .productCode("P005")
                .desiredQuantity(2)
                .build();

        findByCodeValid(purchaseRequest, HttpStatus.OK)
                .returnResult(PurchaseResponse.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(response -> {
                    assertEquals("P005",response.productCode());
                    assertEquals(2, response.desiredQuantity());
                    assertEquals(ProductCategory.ELECTRONICS,response.productCategory());
                })
                .verifyComplete();
    }

    @Test
    void notEnoughInventory() {
        var purchaseRequest = PurchaseRequest.builder()
                .productCode("P005")
                .desiredQuantity(20)
                .build();

        findByCodeInvalid(purchaseRequest, HttpStatus.BAD_REQUEST,"Not Enough Inventory");
    }

    @Test
    void missingProductCode() {
        var purchaseRequest = PurchaseRequest.builder()
                .desiredQuantity(20)
                .build();

        findByCodeInvalid(purchaseRequest, HttpStatus.BAD_REQUEST,"Invalid Product Request");
    }

    @Test
    void missingQuantity() {
        var purchaseRequest = PurchaseRequest.builder()
                .productCode("P005")
                .build();

        findByCodeInvalid(purchaseRequest, HttpStatus.BAD_REQUEST,"Invalid Product Request");
    }

    @Test
    void notFoundProduct() {
        var purchaseRequest = PurchaseRequest.builder()
                .productCode("P00400")
                .desiredQuantity(20)
                .build();

        findByCodeInvalid(purchaseRequest, HttpStatus.NOT_FOUND,"Product Not Found");
    }
}
