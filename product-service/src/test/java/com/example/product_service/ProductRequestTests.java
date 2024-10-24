package com.example.product_service;

import com.example.product_service.domain.ProductCategory;
import com.example.product_service.dto.ProductRequest;
import com.example.product_service.dto.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductRequestTests extends AbstractTest {
    public WebTestClient.ResponseSpec createProduct(ProductRequest productRequest, HttpStatus expectedStatus) {
        return client
                .post()
                .uri("/products")
                .bodyValue(productRequest)
                .exchange()
                .expectStatus()
                .isEqualTo(expectedStatus);
    }

    public Duration invalidProductRequest(ProductRequest productRequest, String detail) {
        return client
                .post()
                .uri("/products")
                .bodyValue(productRequest)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.BAD_REQUEST)
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problemDetail -> {
                    assertEquals("Invalid Product Request", problemDetail.getTitle());
                    assertEquals(detail, problemDetail.getDetail());
                })
                .verifyComplete();
    }

    @Test
    void testCreateProduct() {
        var request = ProductRequest.builder()
                .productCategory(ProductCategory.ACCESSORIES)
                .productCode("P0088")
                .basePrice(100)
                .availableQuantity(15)
                .build();
        createProduct(request, HttpStatus.OK)
                .returnResult(ProductResponse.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(response -> {
                    assertEquals(100,response.basePrice());
                    assertEquals(15,response.availableQuantity());
                    assertEquals("P0088",response.productCode());
                    assertEquals(ProductCategory.ACCESSORIES,response.productCategory());
                })
                .verifyComplete();
    }

    @Test
    void missingProductCategory() {
        var request = ProductRequest.builder()
                .productCode("P0088")
                .basePrice(100)
                .availableQuantity(15)
                .build();

        invalidProductRequest(request, "Product category is required");
    }

    @Test
    void missingProductCode() {
        var request = ProductRequest.builder()
                .productCategory(ProductCategory.ACCESSORIES)
                .basePrice(100)
                .availableQuantity(15)
                .build();

        invalidProductRequest(request, "Product code is required");
    }

    @Test
    void missingProductPrice() {
        var request = ProductRequest.builder()
                .productCategory(ProductCategory.ACCESSORIES)
                .productCode("P0088")
                .availableQuantity(15)
                .build();

        invalidProductRequest(request, "Product price is required and should be positive");
    }

    @Test
    void missingProductQuantity() {
        var request = ProductRequest.builder()
                .productCategory(ProductCategory.ACCESSORIES)
                .productCode("P0088")
                .basePrice(100)
                .build();

        invalidProductRequest(request, "Product available quantity is required and should be positive");
    }
}
