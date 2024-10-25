package com.example.aggregator_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;


@SpringBootTest
@AutoConfigureWebTestClient
public abstract class AbstractTest {
    @Autowired
    protected WebTestClient client;



}
