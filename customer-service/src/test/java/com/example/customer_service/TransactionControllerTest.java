package com.example.customer_service;

import com.example.customer_service.domain.OrderStatus;
import com.example.customer_service.domain.ProductCategory;
import com.example.customer_service.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionControllerTest extends AbstractTest{
    public WebTestClient.ResponseSpec buy(Integer customerId, PurchaseRequest request, HttpStatus expectedStatus) {
        return client
                .post()
                .uri("/customers/{customerId}/buy",customerId)
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isEqualTo(expectedStatus);
    }

    public WebTestClient.ResponseSpec cancel(Integer customerId, CancelPurchaseRequest request, HttpStatus expectedStatus) {
        return client
                .post()
                .uri("/customers/{customerId}/cancel",customerId)
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isEqualTo(expectedStatus);
    }

    public WebTestClient.ResponseSpec findById(Integer customerId, HttpStatus expectedStatus) {
        return client
                .get()
                .uri("/customers/{customerId}",customerId)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus);
    }

    @Test
    void validBuyRequest(){
        var request = PurchaseRequest.builder()
                .price(100)
                .productCategory(ProductCategory.ACCESSORIES)
                .productCode("ABC")
                .quantity(10)
                .build();
        buy(3,request,HttpStatus.OK)
                .returnResult(PurchaseResponse.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(purchaseResponse -> {
                    assertEquals("ABC",purchaseResponse.productCode());
                    assertEquals(100,purchaseResponse.price());
                    assertEquals(ProductCategory.ACCESSORIES,purchaseResponse.productCategory());
                    assertEquals(10,purchaseResponse.quantity());
                    assertEquals(1000, purchaseResponse.totalPrice());
                    assertEquals(1000, purchaseResponse.balance());
                    assertEquals("Charlie", purchaseResponse.customerName());
                    assertEquals(2, purchaseResponse.orderId());
                    assertEquals(OrderStatus.PENDING, purchaseResponse.orderStatus());
                })
                .verifyComplete();

        findById(3,HttpStatus.OK)
                .returnResult(CustomerInformation.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(customerInformation -> {
                    assertEquals("Charlie",customerInformation.name());
                    assertEquals(1000,customerInformation.balance());
                    assertEquals("789 Pine Rd",customerInformation.shippingAddress());
                    var orderSummaryList = customerInformation.orderSummaries();
                    var firstOrder = orderSummaryList.getFirst();
                    assertEquals(10, firstOrder.quantity());
                    assertEquals(100, firstOrder.price());
                    assertEquals("ABC",firstOrder.productCode());
                    assertEquals(OrderStatus.PENDING,firstOrder.status());

                })
                .verifyComplete();
    }

    @Test
    void notEnoughBalance(){
        var request = PurchaseRequest.builder()
                .price(100)
                .productCategory(ProductCategory.ACCESSORIES)
                .productCode("ABC")
                .quantity(100)
                .build();

        buy(1,request,HttpStatus.BAD_REQUEST)
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problemDetail -> {
                    assertEquals(HttpStatus.BAD_REQUEST.value(),problemDetail.getStatus());
                    assertEquals("Customer 1 does not have enough balance",problemDetail.getDetail());
                    assertEquals("Not enough balance", problemDetail.getTitle());
                })
                .verifyComplete();
    }

    @Test
    void notFoundCustomer(){
        var request = PurchaseRequest.builder()
                .price(100)
                .productCategory(ProductCategory.ACCESSORIES)
                .productCode("ABC")
                .quantity(10)
                .build();

        buy(55,request,HttpStatus.NOT_FOUND)
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problemDetail -> {
                    assertEquals(HttpStatus.NOT_FOUND.value(),problemDetail.getStatus());
                    assertEquals("Customer with id 55 not found",problemDetail.getDetail());
                    assertEquals("Customer not found", problemDetail.getTitle());
                })
                .verifyComplete();
    }

    @Test
    void invalidPrice(){
        var request = PurchaseRequest.builder()
                .productCategory(ProductCategory.ACCESSORIES)
                .productCode("ABC")
                .quantity(10)
                .build();

        buy(1,request,HttpStatus.BAD_REQUEST)
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problemDetail -> {
                    assertEquals(HttpStatus.BAD_REQUEST.value(),problemDetail.getStatus());
                    assertEquals("Price is required and should be positive",problemDetail.getDetail());
                    assertEquals("Purchase request is invalid", problemDetail.getTitle());
                })
                .verifyComplete();

        var request1 = PurchaseRequest.builder()
                .productCategory(ProductCategory.ACCESSORIES)
                .price(0)
                .productCode("ABC")
                .quantity(10)
                .build();

        buy(1,request1,HttpStatus.BAD_REQUEST)
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problemDetail -> {
                    assertEquals(HttpStatus.BAD_REQUEST.value(),problemDetail.getStatus());
                    assertEquals("Price is required and should be positive",problemDetail.getDetail());
                    assertEquals("Purchase request is invalid", problemDetail.getTitle());
                })
                .verifyComplete();
    }

    @Test
    void invalidQuantity(){
        var request = PurchaseRequest.builder()
                .productCategory(ProductCategory.ACCESSORIES)
                .productCode("ABC")
                .price(10)
                .build();

        buy(1,request,HttpStatus.BAD_REQUEST)
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problemDetail -> {
                    assertEquals(HttpStatus.BAD_REQUEST.value(),problemDetail.getStatus());
                    assertEquals("Quantity is required and should be positive",problemDetail.getDetail());
                    assertEquals("Purchase request is invalid", problemDetail.getTitle());
                })
                .verifyComplete();

        var request1 = PurchaseRequest.builder()
                .productCategory(ProductCategory.ACCESSORIES)
                .price(10)
                .productCode("ABC")
                .quantity(0)
                .build();

        buy(1,request1,HttpStatus.BAD_REQUEST)
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problemDetail -> {
                    assertEquals(HttpStatus.BAD_REQUEST.value(),problemDetail.getStatus());
                    assertEquals("Quantity is required and should be positive",problemDetail.getDetail());
                    assertEquals("Purchase request is invalid", problemDetail.getTitle());
                })
                .verifyComplete();
    }

    @Test
    void missingCode(){
        var request = PurchaseRequest.builder()
                .productCategory(ProductCategory.ACCESSORIES)
                .price(10)
                .quantity(10)
                .build();

        buy(1,request,HttpStatus.BAD_REQUEST)
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problemDetail -> {
                    assertEquals(HttpStatus.BAD_REQUEST.value(),problemDetail.getStatus());
                    assertEquals("Product code is required",problemDetail.getDetail());
                    assertEquals("Purchase request is invalid", problemDetail.getTitle());
                })
                .verifyComplete();

        var request1 = PurchaseRequest.builder()
                .productCategory(ProductCategory.ACCESSORIES)
                .price(10)
                .quantity(10)
                .build();

        buy(1,request1,HttpStatus.BAD_REQUEST)
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problemDetail -> {
                    assertEquals(HttpStatus.BAD_REQUEST.value(),problemDetail.getStatus());
                    assertEquals("Product code is required",problemDetail.getDetail());
                    assertEquals("Purchase request is invalid", problemDetail.getTitle());
                })
                .verifyComplete();
    }

    @Test
    void missingCategory(){
        var request = PurchaseRequest.builder()
                .price(10)
                .quantity(10)
                .productCode("ABC")
                .build();

        buy(1,request,HttpStatus.BAD_REQUEST)
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problemDetail -> {
                    assertEquals(HttpStatus.BAD_REQUEST.value(),problemDetail.getStatus());
                    assertEquals("Category is required",problemDetail.getDetail());
                    assertEquals("Purchase request is invalid", problemDetail.getTitle());
                })
                .verifyComplete();

        var request1 = PurchaseRequest.builder()
                .price(10)
                .quantity(10)
                .productCode("ABC")
                .build();

        buy(1,request1,HttpStatus.BAD_REQUEST)
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problemDetail -> {
                    assertEquals(HttpStatus.BAD_REQUEST.value(),problemDetail.getStatus());
                    assertEquals("Category is required",problemDetail.getDetail());
                    assertEquals("Purchase request is invalid", problemDetail.getTitle());
                })
                .verifyComplete();
    }

    /*
        @Test
    void validCancelRequest(){
        var request = PurchaseRequest.builder()
                .price(100)
                .productCategory(ProductCategory.ACCESSORIES)
                .productCode("ABC")
                .quantity(10)
                .build();
        buy(1,request,HttpStatus.OK)
                .returnResult(PurchaseResponse.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(purchaseResponse -> {
                    assertEquals("ABC",purchaseResponse.productCode());
                    assertEquals(100,purchaseResponse.price());
                    assertEquals(ProductCategory.ACCESSORIES,purchaseResponse.productCategory());
                    assertEquals(10,purchaseResponse.quantity());
                    assertEquals(1000, purchaseResponse.totalPrice());
                    assertEquals(4000, purchaseResponse.balance());
                    assertEquals("Alice", purchaseResponse.customerName());
              //      assertEquals(1, purchaseResponse.orderId());
                    assertEquals(OrderStatus.PENDING, purchaseResponse.orderStatus());
                })
                .verifyComplete();

        var cancelRequest = CancelPurchaseRequest.builder()
                .orderId(1)
                .build();

        cancel(1,cancelRequest,HttpStatus.OK)
                .returnResult(CancelPurchaseResponse.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(cancelResponse -> {
                    assertEquals(1,cancelResponse.orderId());
                    assertEquals(OrderStatus.CANCELLED,cancelResponse.orderStatus());
                    assertEquals(5000,cancelResponse.balance());
                    assertEquals(1,cancelResponse.customerId());
                    assertEquals("ABC",cancelResponse.productCode());
                })
                .verifyComplete();
    }
     */

    @Test
    void orderNotFound(){
        var cancelRequest = CancelPurchaseRequest.builder()
                .orderId(15)
                .build();

        cancel(1,cancelRequest,HttpStatus.NOT_FOUND)
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problemDetail -> {
                    assertEquals(HttpStatus.NOT_FOUND.value(),problemDetail.getStatus());
                    assertEquals("Order not found with id 15",problemDetail.getDetail());
                    assertEquals("Order not found", problemDetail.getTitle());
                })
                .verifyComplete();
    }

    @Test
    void orderCannotBeCancelled(){
        var request = PurchaseRequest.builder()
                .price(100)
                .productCategory(ProductCategory.ACCESSORIES)
                .productCode("ABC")
                .quantity(10)
                .build();
        buy(1,request,HttpStatus.OK)
                .returnResult(PurchaseResponse.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(purchaseResponse -> {
                    assertEquals("ABC",purchaseResponse.productCode());
                    assertEquals(100,purchaseResponse.price());
                    assertEquals(ProductCategory.ACCESSORIES,purchaseResponse.productCategory());
                    assertEquals(10,purchaseResponse.quantity());
                    assertEquals(1000, purchaseResponse.totalPrice());
                    assertEquals(4000, purchaseResponse.balance());
                    assertEquals("Alice", purchaseResponse.customerName());
                    assertEquals(1, purchaseResponse.orderId());
                    assertEquals(OrderStatus.PENDING, purchaseResponse.orderStatus());
                })
                .verifyComplete();

        var cancelRequest = CancelPurchaseRequest.builder()
                .orderId(1)
                .build();

        cancel(1,cancelRequest,HttpStatus.OK)
                .returnResult(CancelPurchaseResponse.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(cancelResponse -> {
                    assertEquals(1,cancelResponse.orderId());
                    assertEquals(OrderStatus.CANCELLED,cancelResponse.orderStatus());
                    assertEquals(5000,cancelResponse.balance());
                    assertEquals(1,cancelResponse.customerId());
                    assertEquals("ABC",cancelResponse.productCode());
                })
                .verifyComplete();

        var cancelRequest2 = CancelPurchaseRequest.builder()
                .orderId(1)
                .build();

        cancel(1,cancelRequest2,HttpStatus.BAD_REQUEST)
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(problemDetail -> {
                    assertEquals(HttpStatus.BAD_REQUEST.value(),problemDetail.getStatus());
                    assertEquals("Order with id 1 can't be cancelled",problemDetail.getDetail());
                    assertEquals("Order cannot be cancelled", problemDetail.getTitle());
                })
                .verifyComplete();
    }
}
