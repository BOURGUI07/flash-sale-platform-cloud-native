package com.example.edge_service.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public class WebEndPoints {
    @Bean
    public RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route()
                .GET("/aggregator/customers-fallback", request ->
                        ServerResponse.ok().body(Mono.just("You're seeing fallback for aggregator customers GET endpoint"),String.class))
                .POST("/aggregator/customers-fallback", request ->
                        ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build())
                .DELETE("/aggregator/customers-fallback", request ->
                        ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build())
                .GET("/aggregator/products-fallback", request ->
                        ServerResponse.ok().body(Flux.just("You're seeing fallback for aggregator products GET endpoint"),String.class))
                .POST("/aggregator/products-fallback", request ->
                        ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build())
                .GET("/aggregator/properties-fallback", request ->
                        ServerResponse.ok().body(Mono.just("You're seeing fallback for aggregator properties GET endpoint"),String.class))
                .POST("/aggregator/transactions-fallback", request ->
                        ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build())
                .build();
    }
}
