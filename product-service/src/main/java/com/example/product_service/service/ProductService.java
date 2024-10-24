package com.example.product_service.service;

import com.example.product_service.domain.Product;
import com.example.product_service.dto.*;
import com.example.product_service.exceptions.ApplicationExceptions;
import com.example.product_service.mapper.Mapper;
import com.example.product_service.repo.ProductRepo;
import com.example.product_service.validator.CancelProcessRequestValidator;
import com.example.product_service.validator.ProcessRequestValidator;
import com.example.product_service.validator.ProductRequestValidator;
import com.example.product_service.validator.PurchaseRequestValidator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Random;

@Service
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class ProductService {
    private final Sinks.Many<ProductResponse> sink;
    private final ProductRepo repo;
    private final Random random = new Random();

    @PostConstruct
    public void init() {
        repo.findAll()
                .map(Mapper::toProductResponse)
                .doOnNext(sink::tryEmitNext)
                .subscribe();
    }

    @Scheduled(fixedRate = 5000) // Update prices every 5 seconds
    public void schedulePriceUpdates() {
        updateRandomPrices()
                .subscribe(
                        null,
                        error -> log.error("Error updating prices: ", error)
                );
    }

    @Transactional
    public Mono<ProductResponse> create(Mono<ProductRequest> request) {
        return request
                .transform(ProductRequestValidator.validate())
                .flatMap(req ->
                    repo.existsByProductCode(req.productCode())
                            .filter(x->!x)
                            .switchIfEmpty(ApplicationExceptions.productAlreadyExists(req.productCode()))
                            .thenReturn(req)
                )
                .map(Mapper::toProduct)
                .flatMap(repo::save)
                .map(Mapper::toProductResponse)
                .doOnNext(sink::tryEmitNext);
    }

    public Mono<PurchaseResponse> getProductInfo(Mono<PurchaseRequest> request) {
        return request.transform(PurchaseRequestValidator.validate())
                .zipWhen(req ->{
                    var code = req.productCode();
                    return repo.findByProductCode(code)
                            .switchIfEmpty(ApplicationExceptions.productNotFound(code))
                            .filter(product -> product.getAvailableQuantity()>=req.desiredQuantity())
                            .switchIfEmpty(ApplicationExceptions.notEnoughInventory(code));
                        }
                ).map(x->Mapper.toPurchaseResponse(x.getT2(),x.getT1()));

    }

    public Flux<ProductResponse> getAllProducts(Integer maxPrice) {
        return sink.asFlux()
                .filter(product -> product.currentPrice()<=maxPrice);
    }

    @Transactional
    public Mono<ProductPurchaseProcessResponse> processPurchase(Mono<ProductPurchaseProcessRequest> request){
        return request
                .transform(ProcessRequestValidator.validate())
                .zipWhen(req -> repo.findByProductCode(req.productCode())
                .switchIfEmpty(ApplicationExceptions.productNotFound(req.productCode())))
                .flatMap(x-> executePurchaseProcess(x.getT1(),x.getT2()));

    }

    private Mono<ProductPurchaseProcessResponse> executePurchaseProcess(ProductPurchaseProcessRequest request, Product product) {
        product.setAvailableQuantity(product.getAvailableQuantity()-request.desiredQuantity());
        return repo.save(product)
                .thenReturn(Mapper.toProductPurchaseProcessResponse(request));
    }

    @Transactional
    public Mono<ProductCancelProcessResponse> processCancel(Mono<ProductCancelProcessRequest> request){
        return request
                .transform(CancelProcessRequestValidator.validate())
                .zipWhen(req -> repo.findByProductCode(req.productCode())
                        .switchIfEmpty(ApplicationExceptions.productNotFound(req.productCode())))
                .flatMap(x-> executeCancelProcess(x.getT1(),x.getT2()));

    }

    private Mono<ProductCancelProcessResponse> executeCancelProcess(ProductCancelProcessRequest request, Product product) {
        product.setAvailableQuantity(product.getAvailableQuantity()+request.returnedQuantity());
        return repo.save(product)
                .thenReturn(Mapper.toProductCancelProcessResponse(request));
    }



    @Transactional
    public Mono<Void> updateRandomPrices(){
        return repo.findAll()
                .filter(x-> random.nextBoolean())
                .flatMap(this::updateProductPrice)
                .then();
    }

    private Mono<ProductResponse> updateProductPrice(Product product) {
        var newPrice = calculateNewPrice(product.getBasePrice());
        product.setCurrentPrice(newPrice);
        return repo.save(product)
                .map(Mapper::toProductResponse)
                .doOnNext(sink::tryEmitNext);
    }

    private Integer calculateNewPrice(Integer basePrice) {
        double fluctuation = 0.9 + (random.nextDouble() * 0.2);
        return (int) Math.round(basePrice * fluctuation);
    }
}
