package com.indra.bffservice.service;

import com.indra.bffservice.model.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceClientContext {

    private final IProductServiceClient cachedProductServiceClient;
    private final IProductServiceClient externalProductServiceClient;

    @Autowired
    public ProductServiceClientContext(CachedProductServiceClient cachedProductServiceClient,
                                       ExternalProductServiceClient externalProductServiceClient) {
        this.cachedProductServiceClient = cachedProductServiceClient;
        this.externalProductServiceClient = externalProductServiceClient;
    }

    public Mono<ProductDTO> getProductByUniqueCode(String uniqueCode) {
        return cachedProductServiceClient.getProductByUniqueCode(uniqueCode)
                .onErrorResume(e -> externalProductServiceClient.getProductByUniqueCode(uniqueCode));
    }
}
