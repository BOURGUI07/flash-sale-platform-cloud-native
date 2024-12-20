package com.example.aggregator_service.client;

import com.example.aggregator_service.dto.*;
import com.example.aggregator_service.exceptions.ApplicationExceptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

@RequiredArgsConstructor
@Slf4j
public class ProductServiceClient {
    private final WebClient webClient;
    private final Duration timeout;
    private Flux<ProductResponse> productResponseFlux;


    private <T> Mono<T> handleProductNotFoundException(WebClientResponseException.NotFound exception){
        var problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
        var message = problemDetail!=null? problemDetail.getDetail(): exception.getMessage();
        return ApplicationExceptions.productAlreadyExists(message);
    }

    private <T> Mono<T> handleProductRequestBadRequest(WebClientResponseException.BadRequest exception){
        var problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
        var message = problemDetail!=null? problemDetail.getDetail(): exception.getMessage();

        if(problemDetail!=null){
            var title = problemDetail.getTitle();
            return switch (title){
                case "Product Already Exists" -> ApplicationExceptions.productAlreadyExists(message);
                case "Invalid Product Request" -> ApplicationExceptions.invalidProductRequest(message);
                case "Invalid Product Purchase Request" -> ApplicationExceptions.invalidPurchaseRequest(message);
                case "Not Enough Inventory" -> ApplicationExceptions.notEnoughInventory(message);
                case "Invalid Product Purchase Process Request" -> ApplicationExceptions.invalidProcessRequest(message);
                case "Invalid Product Cancel Process Request" -> ApplicationExceptions.invalidCancelProcessRequest(message);
                default -> ApplicationExceptions.generalBadRequest(message);
            };
        }
        return ApplicationExceptions.generalBadRequest(message);
    }

    public Mono<PurchaseResponse> getProductInfo(PurchaseRequest request) {
        return webClient
                .post()
                .uri("/products/purchase-request")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PurchaseResponse.class)
                .timeout(timeout,ApplicationExceptions.timeoutExpired("Product Service"))
                .onErrorResume(WebClientResponseException.NotFound.class, this::handleProductNotFoundException)
                .onErrorResume(WebClientResponseException.BadRequest.class, this::handleProductRequestBadRequest)
                .retryWhen(backOffRetry())
                .onErrorResume(WebClientResponseException.class, ex->ApplicationExceptions.productException("Get Product Information"));
    }

    public Mono<ProductPurchaseProcessResponse> processPurchase(ProductPurchaseProcessRequest request) {
        return webClient
                .post()
                .uri("/products/process-request")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ProductPurchaseProcessResponse.class)
                .onErrorResume(WebClientResponseException.NotFound.class, this::handleProductNotFoundException)
                .onErrorResume(WebClientResponseException.BadRequest.class, this::handleProductRequestBadRequest)
                .onErrorResume(WebClientResponseException.class, ex->ApplicationExceptions.productException("Process Purchase Request"));
    }

    public Mono<ProductCancelProcessResponse> processCancel(ProductCancelProcessRequest request) {
        return webClient
                .post()
                .uri("/products/process-cancel")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ProductCancelProcessResponse.class)
                .onErrorResume(WebClientResponseException.NotFound.class, this::handleProductNotFoundException)
                .onErrorResume(WebClientResponseException.BadRequest.class, this::handleProductRequestBadRequest)
                .onErrorResume(WebClientResponseException.class, ex->ApplicationExceptions.productException("Process Cancel Request"));
    }

    public Mono<ProductResponse> createProduct(ProductRequest request) {
        return webClient
                .post()
                .uri("/products")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ProductResponse.class)
                .timeout(timeout,ApplicationExceptions.timeoutExpired("Product Service"))
                .onErrorResume(WebClientResponseException.BadRequest.class, this::handleProductRequestBadRequest)
                .onErrorResume(WebClientResponseException.class, ex->ApplicationExceptions.productException("Create Product"));
    }

    public Flux<ProductResponse> productResponseFlux(Integer maxPrice) {
        if(Objects.isNull(productResponseFlux)){
            productResponseFlux=productStream(maxPrice);
        }
        return productResponseFlux;
    }

    private Flux<ProductResponse> productStream(Integer maxPrice) {
        return webClient
                .get()
                .uri("/products/stream/{maxPrice}", maxPrice)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(ProductResponse.class)
                .retryWhen(retry())
                .cache(1)
                .onErrorResume(WebClientResponseException.class, ex->ApplicationExceptions.productException("Get Product Stream"));
    }

    private Retry retry() {
        return Retry.fixedDelay(100, Duration.ofSeconds(1))
                .doBeforeRetry(rs->log.info("PRODUCT SERVICE PRODUCT STREAM FAILED: {} RETRYING",rs.failure().getMessage()));
    }

    private Retry backOffRetry(){
        return Retry.backoff(3, Duration.ofMillis(100))
                .filter(throwable -> throwable instanceof TimeoutException);
        // 3 attempts are allowed, 100 ms as initial backoff.
    }


}
