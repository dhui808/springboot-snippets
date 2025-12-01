package com.example.caching.service;

import com.example.caching.Entity.Product;
import com.example.caching.model.DownstreamResponse;
import com.example.caching.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Use set method (not injection) and @PostConstruct to resolve circular dependency.
 */
@Service
@Slf4j
public class ProductCacheService {

    private ProductService productService;

    public void setProductService(final ProductService productService) {
        this.productService = productService;
    }

    @Cacheable(value = "guids")
    public String getGuidByProductId(long productId) {
        log.info("call getGuidByProductId for {}", productId);
        DownstreamResponse response = productService.getDownstreamResponseByProductId(productId);
        String guid = response.getGuid();

        return guid;
    }

    @CacheEvict(value = "guids", allEntries = true)
    @Scheduled(fixedRateString = "43200000")
    public void clearProductsCache() {
        System.out.println("Clearing products cache");
    }
}
