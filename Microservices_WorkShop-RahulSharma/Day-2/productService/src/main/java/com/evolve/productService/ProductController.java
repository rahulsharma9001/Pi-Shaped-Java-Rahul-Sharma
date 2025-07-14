package com.evolve.productService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ProductController {

    @Value("${message:Default Product Service Message}")
    private String message;

    @GetMapping("/products")
    public String getProducts() {
        return "Product Service: " + message;
    }
}
