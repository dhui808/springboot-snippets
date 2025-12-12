package com.example.restassured.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

/**
 * Abstract base client itself is not annotated with @Service or @Component,
 * but dependency injection works.
 */
public class AbstractClient {
    @Autowired
    protected RestTemplate restTemplate;
}
