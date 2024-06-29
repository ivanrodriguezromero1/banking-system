package com.indra.bffservice.service;

import com.indra.bffservice.model.ProductDTO;
import reactor.core.publisher.Mono;

public interface IProductServiceClient {
    Mono<ProductDTO> getProductByUniqueCode(String uniqueCode);
}
