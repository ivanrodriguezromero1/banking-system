package com.indra.productservice.controller;

import com.indra.productservice.model.ProductDTO;
import com.indra.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public Mono<ResponseEntity<ProductDTO>> getProductById(@PathVariable String productId) {
        return productService.getProductById(productId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
