package com.example.aggregator_service;

import com.example.aggregator_service.domain.OrderStatus;
import com.example.aggregator_service.domain.ProductCategory;
import com.example.aggregator_service.dto.CancelPurchaseRequest;
import com.example.aggregator_service.dto.CancelPurchaseResponse;
import com.example.aggregator_service.dto.CustomerPurchaseResponse;
import com.example.aggregator_service.dto.PurchaseRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class ValidTransactionRequestTest extends AbstractTest {
    @Test
    void buyAndCancel(){
        var request = PurchaseRequest.builder()
                        .productCode("P003")
                                .desiredQuantity(1)
                                        .build();

        var orderId = new AtomicInteger();
        client
                .post()
                .uri("/transactions/{customerId}/buy",3)
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(CustomerPurchaseResponse.class)
                .getResponseBody()
                .doOnNext(response -> {
                    log.info(response.toString());
                    orderId.set(response.orderId());
                })
                .as(StepVerifier::create)
                .assertNext(response -> {
                    assertEquals(ProductCategory.FURNITURE,response.productCategory());
                    assertEquals(1,response.quantity());
                    assertEquals("Charlie",response.customerName());
                    assertEquals("P003",response.productCode());
                    assertTrue(response.balance()<2000);
                    assertEquals(OrderStatus.PENDING,response.orderStatus());
                })
                .verifyComplete();


            var cancelPurchaseRequest = CancelPurchaseRequest.builder()
                .orderId(orderId.get())
                .build();
        client
                .post()
                .uri("/transactions/{customerId}/cancel",3)
                .bodyValue(cancelPurchaseRequest)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(CancelPurchaseResponse.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(response -> {
                    assertEquals(OrderStatus.CANCELLED,response.orderStatus());
                    assertEquals(1,response.returnedQuantity());
                })
                .verifyComplete();


    }




}
