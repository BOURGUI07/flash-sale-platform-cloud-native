package com.example.aggregator_service.controller;

import com.example.aggregator_service.dto.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/properties")
@RequiredArgsConstructor
@Slf4j
public class PropertiesController {
    private final AppProperties appProperties;

    @GetMapping("/greeting")
    public String greeting() {
        log.info("Aggregator Properties Controller::Getting Greeting");
        return appProperties.greeting();
    }
}
