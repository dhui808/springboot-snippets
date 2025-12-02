package com.example.caching.service.postconstruct;

import com.example.caching.Entity.Product;
import com.example.caching.model.DownstreamResponse;
import com.example.caching.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.cache.test.autoconfigure.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

/**
 * Option 1 for enabling Spring Cache.
 *
 * @SpringBootTest annotation indludes the relevant dependency that needs dependency injection, plus
 * a separate configuration class with @AutoConfigureCache, even though it seems redundant. Actaully,
 * without it, the caching does not work.
 */
@SpringBootTest(classes = {ProductCacheService.class, ProductCacheServiceAutoConfigureCacheTest.TestConfig.class})
@ActiveProfiles("test")
@EnableCaching
//@AutoConfigureCache // it does not work if placed here
class ProductCacheServiceAutoConfigureCacheTest {

    @Autowired
    private ProductCacheService productCacheService;

    private ProductService productService;
    @Autowired
    private CacheManager cacheManager;

    @MockitoBean
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        productCacheService.setProductService(productService);

        // clear caches before each test
        cacheManager.getCache("products").clear();
        cacheManager.getCache("recentProducts").clear();
        Mockito.reset(productRepository);
    }

    @Test
    void testGetGuidByProductId() {
        Product prod = mock(Product.class);
        DownstreamResponse response = mock(DownstreamResponse.class);
        String guid = "GUID-12345";

        when(response.getGuid()).thenReturn(guid);
        when(productService.getDownstreamResponseByProductId(anyLong())).thenReturn(response);

        var first = productCacheService.getGuidByProductId(1L);
        var second = productCacheService.getGuidByProductId(1L);

        assertSame(first, second);
        assertSame(guid, first);
        verify(productService, times(1)).getDownstreamResponseByProductId(1L);

        productCacheService.clearProductsCache();
        var third = productCacheService.getGuidByProductId(1L);
        assertSame(guid, third);
        verify(productService, times(2)).getDownstreamResponseByProductId(1L);
    }

    /**
     * The annotation @AutoConfigureCache has to be placed on a separate class. Strange.
     */
    @AutoConfigureCache
    public static class TestConfig {
    }
}