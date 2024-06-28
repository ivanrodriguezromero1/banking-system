package com.indra.productservice.service;

import com.indra.productservice.model.Product;
import com.indra.productservice.model.ProductDTO;
import com.indra.productservice.model.mapper.ProductMapper;
import com.indra.productservice.repository.ProductRepository;
import com.indra.bankingstarter.util.CustomException;
import com.indra.bankingstarter.encryption.EncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private EncryptionService encryptionService;

    @InjectMocks
    private ProductService productService;

    private final ProductMapper productMapper = ProductMapper.INSTANCE;

    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setId(1L);
        product.setName("Savings Account");
        product.setType("Account");
        product.setBalance(1000.0);

        productDTO = productMapper.productToProductDTO(product);
    }

    @Test
    void getProductById_shouldReturnProduct() {
        given(encryptionService.decrypt(anyString())).willReturn("decryptedId");
        given(productRepository.findById(anyString())).willReturn(Mono.just(product));

        Mono<ProductDTO> result = productService.getProductById("encryptedId");

        StepVerifier.create(result)
                .expectNext(productDTO)
                .verifyComplete();
    }

    @Test
    void getProductById_shouldThrowCustomException() {
        given(encryptionService.decrypt(anyString())).willThrow(new CustomException("DECRYPTION_ERROR", "Error decrypting product ID"));

        Mono<ProductDTO> result = productService.getProductById("encryptedId");

        StepVerifier.create(result)
                .expectError(CustomException.class)
                .verify();
    }
}
