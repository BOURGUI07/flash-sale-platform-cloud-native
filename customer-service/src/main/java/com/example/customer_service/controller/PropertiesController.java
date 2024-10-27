package com.example.customer_service.controller;

import com.example.customer_service.dto.AppProperties;
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
        log.info("Customer-Service Properties Controller::Get Greeting");
        return appProperties.greeting();
    }
}
