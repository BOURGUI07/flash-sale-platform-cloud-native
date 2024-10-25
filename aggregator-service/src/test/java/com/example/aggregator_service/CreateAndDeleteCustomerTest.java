package com.example.aggregator_service;

import com.example.aggregator_service.dto.CustomerRequest;
import com.example.aggregator_service.dto.CustomerResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateAndDeleteCustomerTest extends AbstractTest {
    private Duration create(CustomerRequest request, Integer expectedBalance, String expectedName) {
        return client
                .post()
                .uri("/customers")
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.OK)
                .returnResult(CustomerResponse.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(response -> {
                    assertEquals(expectedBalance, response.balance());
                    assertEquals(expectedName, response.name());
                })
                .verifyComplete();
    }

    private Duration invalidCreate(CustomerRequest request, String detail, String title) {
        return client
                .post()
                .uri("/customers")
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.BAD_REQUEST)
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(response -> {
                    assertEquals(detail,response.getDetail());
                    assertEquals(title, response.getTitle());
                })
                .verifyComplete();
    }


    private Duration delete(Integer customerId) {
        return client
                .delete()
                .uri("/customers/{customerId}",customerId)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.OK)
                .returnResult(Void.class)
                .getResponseBody()
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
    }

    private Duration invalidDelete(Integer customerId) {
        return client
                .delete()
                .uri("/customers/{customerId}",customerId)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.NOT_FOUND)
                .returnResult(ProblemDetail.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(response -> {
                    assertEquals("Customer Not Found",response.getTitle());
                    assertEquals("Customer with id " + customerId + " not found",response.getDetail());
                })
                .verifyComplete();
    }

    /*
        @Test
    void createAndDeleteCustomer() {
        var request = CustomerRequest.builder()
                .balance(4000)
                .name("mik")
                .shippingAddress("Marrakech")
                .build();
        create(request,4000,"mik");

        delete(8);

    }
     */

    @Test
    void deleteNonExistingCustomer() {
        invalidDelete(99);
    }

    @Test
    void createAlreadyExistingCustomer() {
        var request = CustomerRequest.builder()
                .balance(4000)
                .name("Alice")
                .shippingAddress("Marrakech")
                .build();

        invalidCreate(request,"Customer Alice already exists","Customer Already Exists");
    }

    @Test
    void postRequestWithMissingName(){
        var request = CustomerRequest.builder()
                .balance(4000)
                .shippingAddress("Marrakech")
                .build();

        invalidCreate(request,"Name is required","Invalid Customer Request");
    }

    @Test
    void postRequestWithMissingBalance(){
        var request = CustomerRequest.builder()
                .shippingAddress("Marrakech")
                .name("Diddy")
                .build();

        invalidCreate(request,"Balance is required","Invalid Customer Request");
    }

    @Test
    void postRequestWithMissingAddress(){
        var request = CustomerRequest.builder()
                .balance(800)
                .name("Diddy")
                .build();

        invalidCreate(request,"Shipping address is required","Invalid Customer Request");
    }
}
