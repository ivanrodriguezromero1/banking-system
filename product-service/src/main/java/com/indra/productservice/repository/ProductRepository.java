package com.indra.productservice.repository;

import com.indra.productservice.model.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {
    Mono<Product> findById(String productId);
}
