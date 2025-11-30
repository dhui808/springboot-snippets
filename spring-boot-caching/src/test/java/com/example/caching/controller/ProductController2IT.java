package com.example.caching.controller;

import com.example.caching.Entity.Product;
import com.example.caching.annotation.IntegrationTest2;
import com.example.caching.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Component scaning and dependency injection will happen since @IntegrationTest2 annotation
 * is meta-annotated with @SpringBootTest(classes = {SpringBootCachingApplication.class}).
 */
@IntegrationTest2
class ProductController2IT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductRepository productRepository;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // clear caches to avoid cross-test interference
        if (cacheManager.getCache("products") != null) cacheManager.getCache("products").clear();
        if (cacheManager.getCache("recentProducts") != null) cacheManager.getCache("recentProducts").clear();
        Mockito.reset(productRepository);
    }

    @Test
    void getAllProducts_returnsList() throws Exception {
        Product p = new Product();
        p.setId(1L);
        when(productRepository.findAll()).thenReturn(List.of(p));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductById_isCached_afterFirstCall() throws Exception {
        Product p = new Product();
        p.setId(2L);
        when(productRepository.findById(2L)).thenReturn(Optional.of(p));

        mockMvc.perform(get("/products/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));

        mockMvc.perform(get("/products/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));

        // service caching should cause repository to be called only once
        verify(productRepository, times(1)).findById(2L);
    }

    @Test
    void addUpdateAndDeleteProduct_endpointsInvokeRepository() throws Exception {
        // POST (add)
        Product toCreate = new Product();
        toCreate.setPrice(9.99);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product arg = invocation.getArgument(0);
            arg.setId(5L);
            return arg;
        });

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));

        verify(productRepository, times(1)).save(any(Product.class));

        // PUT (update)
        Product toUpdate = new Product();
        toUpdate.setPrice(19.99);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product arg = invocation.getArgument(0);
            return arg;
        });

        mockMvc.perform(put("/products/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));

        verify(productRepository, times(2)).save(any(Product.class)); // once for POST, once for PUT

        // DELETE
        mockMvc.perform(delete("/products/5"))
                .andExpect(status().isOk());

        verify(productRepository, times(1)).deleteById(5L);
    }
}
