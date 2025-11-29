package com.example.caching.service;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableCaching
@Import(ProductService.class)
public class TestConfig {

    @Bean
    public CacheManager cacheManager() {
        // caches used by ProductService
        return new ConcurrentMapCacheManager("products", "recentProducts");
    }
}

