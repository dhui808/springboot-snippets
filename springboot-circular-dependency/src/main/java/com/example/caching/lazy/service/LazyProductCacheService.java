package com.example.caching.lazy.service;

import com.example.caching.model.DownstreamResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Use lazy setter injection for both beans to resolve circular dependency.
 */
@Service
@Slf4j
public class LazyProductCacheService {

    private LazyProductService productService;

    @Autowired
    public void setProductService(@Lazy LazyProductService productService) {
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
