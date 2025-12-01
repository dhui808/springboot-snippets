package com.example.caching.service;

import com.example.caching.Entity.Product;
import com.example.caching.model.DownstreamResponse;
import com.example.caching.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Use set method (not injection) and @PostConstruct to resolve circular dependency.
 */
@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCacheService productCacheService;

    public ProductService(ProductRepository productRepository, ProductCacheService productCacheService) {
        this.productRepository = productRepository;
        this.productCacheService = productCacheService;
    }

    @PostConstruct
    public void init() {
        productCacheService.setProductService(this);
    }

    @Cacheable("products")
    public List<Product> getAllProducts() {
		log.info("getAllProducts() is called");
        List<Product> products = productRepository.findAll();
        log.info("Fetched {} products from database", products.size());
        return products;
    }

    @Cacheable(value = "products", key = "#productId", condition = "#productId > 0")
    public Product getProductById(long productId) {
        log.info("getProductById is called for {}", productId);
        return productRepository.findById(productId).orElse(null);
    }

    @CachePut(value = "products", key = "#product.id")
    public Product updateProduct(Product product) {
        Product updatedProduct = productRepository.save(product);
        return updatedProduct;
    }

    @CacheEvict(value = "products", key = "#productId")
    public void deleteProduct(long productId) {
        productRepository.deleteById(productId);
    }

    @Caching(
            cacheable = {@Cacheable(value = "products", key = "#product.id")},
            evict = {@CacheEvict(value = "recentProducts", allEntries = true)}
    )
    public Product getProductDetails(Product product) {
        // Logic to fetch detailed product information
        return productRepository.findById(product.getId()).orElse(null);
    }

    @Cacheable(value = "products", key = "#productId", unless = "#result.price < 100")
    public Product getProductWithPriceCheck(long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @CacheEvict(value = "products", allEntries = true)
    public void clearProductCache() {
        System.out.println("Product cache was cleared successfully");
    }

    @CacheEvict(value = "products", allEntries = true)
    @Scheduled(fixedRateString = "43200000")
    public void clearProductsCache() {
        System.out.println("Clearing products cache");
    }

    public DownstreamResponse getDownstreamResponseByProductId(long productId) {

        // Simulate a downstream call to get DownstreamResponse by productId ID
        UUID guid = UUID.randomUUID();
        DownstreamResponse response = DownstreamResponse.builder()
                .status("Active")
                .data("Sample data for product " + productId)
                .message("Downstream call successful")
                .guid(guid.toString())
                .build();

        log.info("Generated GUID {} for productId {}", guid, productId);

        return response;
    }
}
