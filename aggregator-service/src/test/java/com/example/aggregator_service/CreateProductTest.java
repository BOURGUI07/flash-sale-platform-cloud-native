package com.example.aggregator_service;

import com.example.aggregator_service.domain.ProductCategory;
import com.example.aggregator_service.dto.ProductRequest;
import com.example.aggregator_service.dto.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateProductTest extends AbstractTest {
    private Duration create(ProductRequest request, String expectedCode, Integer expectedQuantity, Integer expectedPrice) {
        return client
                .post()
                .uri("/products")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .returnResult(ProductResponse.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(response -> {
                    assertEquals(expectedCode,response.productCode());
                    assertEquals(expectedQuantity,response.availableQuantity());
                    assertEquals(expectedPrice,response.basePrice());
                })
                .verifyComplete();
    }

    private Duration invalidCreate(ProductRequest request, String detail, String title) {
        return client
                .post()
                .uri("/products")
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(response -> {
                    assertEquals(detail,response.getDetail());
                    assertEquals(title,response.getTitle());
                })
                .verifyComplete();
    }

    /*
        @Test
    void validPostRequest(){
        var request = ProductRequest.builder()
                .availableQuantity(10)
                .productCode("P0097")
                .basePrice(8)
                .productCategory(ProductCategory.ACCESSORIES)
                .build();

        create(request,"P0097",10,8);
    }
     */

    @Test
    void createAlreadyExistingProduct(){
        var request = ProductRequest.builder()
                .availableQuantity(10)
                .productCode("P009")
                .basePrice(8)
                .productCategory(ProductCategory.ACCESSORIES)
                .build();

        invalidCreate(request,"Product with code P009 already exists","Product Already Exists");
    }

    @Test
    void postRequestWithMissingCode(){
        var request = ProductRequest.builder()
                .availableQuantity(10)
                .basePrice(8)
                .productCategory(ProductCategory.ACCESSORIES)
                .build();
        invalidCreate(request,"Product code is required","Invalid Product Request");
    }

    @Test
    void postRequestWithMissingCategory(){
        var request = ProductRequest.builder()
                .availableQuantity(10)
                .productCode("P009")
                .basePrice(8)
                .build();
        invalidCreate(request,"Product category is required","Invalid Product Request");

    }

    @Test
    void postRequestWithMissingPrice(){
        var request = ProductRequest.builder()
                .availableQuantity(10)
                .productCode("P009")
                .productCategory(ProductCategory.ACCESSORIES)
                .build();
        invalidCreate(request,"Product price is required and should be positive","Invalid Product Request");

    }

    @Test
    void postRequestWithMissingQuantity(){
        var request = ProductRequest.builder()
                .productCode("P009")
                .basePrice(8)
                .productCategory(ProductCategory.ACCESSORIES)
                .build();
        invalidCreate(request,"Product available quantity is required and should be positive","Invalid Product Request");

    }
}
