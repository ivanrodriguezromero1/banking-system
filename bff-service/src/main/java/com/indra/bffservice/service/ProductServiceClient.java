package com.indra.bffservice.service;

import com.indra.bffservice.model.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class ProductServiceClient {

    private final WebClient webClient;

    @Autowired
    public ProductServiceClient(WebClient productServiceWebClient) {
        this.webClient = productServiceWebClient;
    }

    public Flux<ProductDTO> getProductsByCustomerId(String customerId) {
        return Flux.deferContextual(ctx -> {
            String traceId = ctx.getOrDefault("traceId", "");
            return webClient.get()
                    .uri("/customer/{customerId}", customerId)
                    .header("traceId", traceId)
                    .retrieve()
                    .bodyToFlux(ProductDTO.class);
        });
    }
}
