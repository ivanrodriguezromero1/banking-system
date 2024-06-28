package com.indra.productservice.service;

import com.indra.bankingstarter.encryption.EncryptionService;
import com.indra.productservice.model.ProductDTO;
import com.indra.productservice.model.mapper.ProductMapper;
import com.indra.productservice.repository.ProductRepository;
import com.indra.bankingstarter.util.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper = ProductMapper.INSTANCE;
    private final EncryptionService encryptionService;

    public Mono<ProductDTO> getProductById(String productId) {
        String decryptedProductId;
        try {
            decryptedProductId = encryptionService.decrypt(productId);
        } catch (Exception e) {
            throw new CustomException("DECRYPTION_ERROR", "Error decrypting product ID", e);
        }
        return productRepository.findById(decryptedProductId)
                .map(productMapper::productToProductDTO)
                .switchIfEmpty(Mono.error(new CustomException("NOT_FOUND", "Product not found")));
    }
}
