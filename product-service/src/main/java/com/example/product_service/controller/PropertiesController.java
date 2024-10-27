package com.example.product_service.controller;

import com.example.product_service.dto.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/properties")
@Slf4j
public class PropertiesController {
    private final AppProperties appProperties;

    @GetMapping("/greeting")
    public String greeting() {
        log.info("Product-Service Properties Controller::Get Greeting");
        return appProperties.greeting();
    }
}
