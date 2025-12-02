package com.example.caching.service.postconstruct;

import com.example.caching.model.DownstreamResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
