package com.example.caching.controller;

import com.example.caching.Entity.Product;
import com.example.caching.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable long productId) {
        return productService.getProductById(productId);
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productService.updateProduct(product);
    }

    @PutMapping("/{productId}")
    public Product updateProduct(@PathVariable long productId, @RequestBody Product product) {
        product.setId(productId);
        return productService.updateProduct(product);
    }

    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable long productId) {
        productService.deleteProduct(productId);
    }

    @GetMapping("/details/{productId}")
    public Product getProductDetails(@PathVariable long productId) {
        Product product = new Product();
        product.setId(productId);
        return productService.getProductDetails(product);
    }

    @GetMapping("/price-check")
    public Product getProductWithPriceCheck(@RequestParam long productId) {
        return productService.getProductWithPriceCheck(productId);
    }

    @PostMapping("/clear-cache")
    public void clearProductCache() {
        productService.clearProductCache();
    }

}
