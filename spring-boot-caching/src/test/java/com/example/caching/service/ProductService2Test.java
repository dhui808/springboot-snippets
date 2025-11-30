package com.example.caching.service;

import com.example.caching.Entity.Product;
import com.example.caching.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

/**
 * Option 2 for enabling Spring Cache.
 *
 * Note that @SpringBootTest loads ALL application components and configuration,
 * so no need to create CacheManger bean and import ProductService. Also, no need
 * to use @AutoConfigureCache and @EnableCaching.
 */
@SpringBootTest
class ProductService2Test {

    @Autowired
    private ProductService productService;

    @Autowired
    private CacheManager cacheManager;

    @MockitoBean
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        // clear caches before each test
        cacheManager.getCache("products").clear();
        cacheManager.getCache("recentProducts").clear();
        Mockito.reset(productRepository);
    }

    @Test
    void testGetAllProducts_isCached() {
        Product prod = mock(Product.class);
        when(productRepository.findAll()).thenReturn(List.of(prod));

        var first = productService.getAllProducts();
        var second = productService.getAllProducts();

        assertSame(first, second);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById_cacheCondition() {
        Product prod = mock(Product.class);
        when(productRepository.findById(1L)).thenReturn(Optional.of(prod));
        when(productRepository.findById(0L)).thenReturn(Optional.of(prod));

        // id > 0 => should be cached
        productService.getProductById(1L);
        productService.getProductById(1L);
        verify(productRepository, times(1)).findById(1L);

        // id == 0 => condition prevents caching, repository called each time
        productService.getProductById(0L);
        productService.getProductById(0L);
        verify(productRepository, times(2)).findById(0L);
    }

    @Test
    void testUpdateAndDeleteProduct_callsRepository() {
        Product prod = mock(Product.class);
        when(prod.getId()).thenReturn(5L);
        when(productRepository.save(prod)).thenReturn(prod);

        var updated = productService.updateProduct(prod);
        assertSame(prod, updated);
        verify(productRepository, times(1)).save(prod);

        productService.deleteProduct(5L);
        verify(productRepository, times(1)).deleteById(5L);
    }

    @Test
    void testGetProductWithPriceCheck_unlessAndCache() {
        // price < 100 => not cached
        Product cheap = mock(Product.class);
        when(cheap.getPrice()).thenReturn(50.0);
        when(productRepository.findById(10L)).thenReturn(Optional.of(cheap));

        productService.getProductWithPriceCheck(10L);
        productService.getProductWithPriceCheck(10L);
        verify(productRepository, times(2)).findById(10L);

        // reset and test expensive product => should be cached
        reset(productRepository);
        Product expensive = mock(Product.class);
        when(expensive.getPrice()).thenReturn(150.0);
        when(productRepository.findById(11L)).thenReturn(Optional.of(expensive));

        productService.getProductWithPriceCheck(11L);
        productService.getProductWithPriceCheck(11L);
        verify(productRepository, times(1)).findById(11L);
    }

    @Test
    void testClearProductCache_effective() {
        Product first = mock(Product.class);
        Product second = mock(Product.class);

        when(productRepository.findAll()).thenReturn(List.of(first));
        var a = productService.getAllProducts();
        verify(productRepository, times(1)).findAll();

        // change repository return and ensure cache clears on evict
        when(productRepository.findAll()).thenReturn(List.of(second));
        productService.clearProductCache(); // evict all entries
        var b = productService.getAllProducts();
        verify(productRepository, times(2)).findAll();

        assertNotSame(a, b);
    }

    @Test
    void testGetProductDetails_and_clearRecentProductsOnCaching() {
        Product input = mock(Product.class);
        when(input.getId()).thenReturn(20L);

        Product found = mock(Product.class);
        when(productRepository.findById(20L)).thenReturn(Optional.of(found));

        var result = productService.getProductDetails(input);
        assertSame(found, result);
        verify(productRepository, times(1)).findById(20L);

        // call again to ensure product caching by id happens
        productService.getProductDetails(input);
        verify(productRepository, times(1)).findById(20L);
    }

    @Test
    void testScheduledClearProductsCache_methodCanBeCalled() {
        Product p = mock(Product.class);
        when(productRepository.findAll()).thenReturn(List.of(p));
        productService.getAllProducts();
        verify(productRepository, times(1)).findAll();

        // invoke scheduled evict method directly
        productService.clearProductsCache();
        // after clear, next call should hit repository again
        productService.getAllProducts();
        verify(productRepository, times(2)).findAll();
    }
}