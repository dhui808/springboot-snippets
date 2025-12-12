package com.example.restassured.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api.service") // Matches the prefix in the properties file
@Data
public class EmployeeApiProperties {
    private String baseUrl;
    private int timeoutMs;
    private String apiKey;
}