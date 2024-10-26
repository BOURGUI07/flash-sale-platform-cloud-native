package com.example.product_service.controller;

import com.example.product_service.dto.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/properties")
public class PropertiesController {
    private final AppProperties appProperties;

    @GetMapping("/greeting")
    public String greeting() {
        return appProperties.greeting();
    }
}
