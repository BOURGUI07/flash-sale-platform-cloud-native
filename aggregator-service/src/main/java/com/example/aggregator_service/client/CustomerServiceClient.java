package com.example.aggregator_service.client;

import com.example.aggregator_service.config.ClientProperties;
import com.example.aggregator_service.dto.*;
import com.example.aggregator_service.exceptions.ApplicationExceptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@RequiredArgsConstructor
@Slf4j
public class CustomerServiceClient {
    private final WebClient webClient;
    private final Duration timeout;


    public Mono<CustomerInformation> getCustomerInformation(Integer customerId) {
        return webClient
                .get()
                .uri("/customers/{customerId}",customerId)
                .retrieve()
                .bodyToMono(CustomerInformation.class)
                .timeout(timeout,ApplicationExceptions.timeoutExpired("Customer Service"))
                .onErrorResume(WebClientResponseException.NotFound.class,this::handleNotFoundException)
                .retryWhen(backOffRetry())
                .onErrorResume(WebClientResponseException.InternalServerError.class, ex->ApplicationExceptions.customerException("Get Customer Information"));
    }

    private <T> Mono<T> handleNotFoundException(WebClientResponseException.NotFound exception){
        var problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
        var message = problemDetail!=null? problemDetail.getDetail(): exception.getMessage();
        if(problemDetail!=null){
            var title = problemDetail.getTitle();
            return switch(title){
                case "Customer not found" -> ApplicationExceptions.customerNotFound(message);
                default -> ApplicationExceptions.orderNotFound(message);
            };
        }
        return ApplicationExceptions.generalNotFound(message);
    }


    public Mono<CustomerResponse> createCustomer(CustomerRequest customerRequest) {
        return webClient
                .post()
                .uri("/customers")
                .bodyValue(customerRequest)
                .retrieve()
                .bodyToMono(CustomerResponse.class)
                .timeout(timeout,ApplicationExceptions.timeoutExpired("Customer Service"))
                .onErrorResume(WebClientResponseException.BadRequest.class,this::handleCustomerRequestBadRequest)
                .onErrorResume(WebClientResponseException.class, ex->ApplicationExceptions.customerException("Create Customer"));
    }

    private <T> Mono<T> handleCustomerRequestBadRequest(WebClientResponseException.BadRequest exception){
        var problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
        var message = problemDetail!=null? problemDetail.getDetail(): exception.getMessage();

        if(problemDetail!=null){
            var title = problemDetail.getTitle();
            return switch (title){
                case "Customer already exists" -> ApplicationExceptions.customerAlreadyExists(message);
                case "Customer request is invalid" -> ApplicationExceptions.invalidCustomerRequest(message);
                case "Purchase request is invalid" -> ApplicationExceptions.invalidPurchaseRequest(message);
                case "Not enough balance" -> ApplicationExceptions.notEnoughBalanceBalance(message);
                case "Cancel request is invalid" -> ApplicationExceptions.invalidCancelRequest(message);
                case "Order cannot be cancelled" -> ApplicationExceptions.cannotBeCancelled(message);
                default -> ApplicationExceptions.generalBadRequest(message);
            };
        }
        return ApplicationExceptions.generalBadRequest(message);
    }

    public Mono<Void> deleteCustomer(Integer customerId) {
        return webClient
                .delete()
                .uri("/customers/{customerId}",customerId)
                .retrieve()
                .bodyToMono(Void.class)
                .timeout(timeout,ApplicationExceptions.timeoutExpired("Customer Service"))
                .onErrorResume(WebClientResponseException.NotFound.class,this::handleNotFoundException)
                .onErrorResume(WebClientResponseException.class, ex->ApplicationExceptions.customerException("Delete Customer"));
    }

    public Mono<CustomerPurchaseResponse> buy(Integer customerId, CustomerPurchaseRequest request){
        return webClient
                .post()
                .uri("/customers/{customerId}/buy",customerId)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CustomerPurchaseResponse.class)
                .timeout(timeout,ApplicationExceptions.timeoutExpired("Customer Service"))
                .onErrorResume(WebClientResponseException.NotFound.class,this::handleNotFoundException)
                .onErrorResume(WebClientResponseException.BadRequest.class,this::handleCustomerRequestBadRequest)
                .onErrorResume(WebClientResponseException.class, ex->ApplicationExceptions.customerException("Buy Product"));
    }

    public Mono<CancelPurchaseResponse> cancel(Integer customerId, CancelPurchaseRequest request){
        return webClient
                .post()
                .uri("/customers/{customerId}/cancel",customerId)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CancelPurchaseResponse.class)
                .timeout(timeout,ApplicationExceptions.timeoutExpired("Customer Service"))
                .onErrorResume(WebClientResponseException.NotFound.class,this::handleNotFoundException)
                .onErrorResume(WebClientResponseException.BadRequest.class,this::handleCustomerRequestBadRequest)
                .onErrorResume(WebClientResponseException.class, ex->ApplicationExceptions.customerException("Cancel Purchase Request"));
    }

    private Retry backOffRetry(){
        return Retry.backoff(3, Duration.ofMillis(100))
                .filter(throwable -> throwable instanceof TimeoutException);
        // 3 attempts are allowed, 100 ms as initial backoff.
    }


}
