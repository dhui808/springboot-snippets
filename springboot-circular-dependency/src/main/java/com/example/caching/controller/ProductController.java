// java
package com.example.caching.controller;

import com.example.caching.Entity.Product;
import com.example.caching.service.postconstruct.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@Slf4j
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
        log.info("ProductController initialized by Spring IOC");
    }

    @GetMapping
    public List<Product> getAllProducts() {
        log.info("GET /products - retrieving all products");
        return productService.getAllProducts();
    }

    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable long productId) {
        log.info("GET /products/{} - retrieving product by id", productId);
        return productService.getProductById(productId);
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        log.info("POST /products - adding product: {}", product);
        return productService.updateProduct(product);
    }

    @PutMapping("/{productId}")
    public Product updateProduct(@PathVariable long productId, @RequestBody Product product) {
        log.info("PUT /products/{} - updating product: {}", productId, product);
        product.setId(productId);
        return productService.updateProduct(product);
    }

    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable long productId) {
        log.info("DELETE /products/{} - deleting product", productId);
        productService.deleteProduct(productId);
    }

    @GetMapping("/details/{productId}")
    public Product getProductDetails(@PathVariable long productId) {
        log.info("GET /products/details/{} - retrieving product details", productId);
        Product product = new Product();
        product.setId(productId);
        return productService.getProductDetails(product);
    }

    @GetMapping("/price-check")
    public Product getProductWithPriceCheck(@RequestParam long productId) {
        log.info("GET /products/price-check?productId={} - price check", productId);
        return productService.getProductWithPriceCheck(productId);
    }

    @PostMapping("/clear-cache")
    public void clearProductCache() {
        log.info("POST /products/clear-cache - clearing product cache");
        productService.clearProductCache();
    }

}