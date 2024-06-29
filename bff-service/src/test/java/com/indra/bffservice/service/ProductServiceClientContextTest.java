package com.indra.bffservice.service;

import com.indra.bffservice.model.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

class ProductServiceClientContextTest {

    @Mock
    private IProductServiceClient cachedProductServiceClient;

    @Mock
    private IProductServiceClient externalProductServiceClient;

    @InjectMocks
    private ProductServiceClientContext productServiceClientContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProductByUniqueCode_shouldReturnProductFromCache() {
        ProductDTO mockProduct = new ProductDTO();
        mockProduct.setId("1");
        mockProduct.setName("Product A");
        mockProduct.setType("Type A");
        mockProduct.setBalance( 100.0);
        mockProduct.setTraceId("traceId");
        given(cachedProductServiceClient.getProductByUniqueCode(anyString())).willReturn(Mono.just(mockProduct));

        Mono<ProductDTO> result = productServiceClientContext.getProductByUniqueCode("encryptedCode")
                .contextWrite(ctx -> ctx.put("traceId", "traceId"));

        StepVerifier.create(result)
                .expectNext(mockProduct)
                .verifyComplete();
    }

    @Test
    void getProductByUniqueCode_shouldReturnProductFromExternalWhenCacheFails() {
        ProductDTO mockProduct = new ProductDTO();
        mockProduct.setId("1");
        mockProduct.setName("Product A");
        mockProduct.setType("Type A");
        mockProduct.setBalance( 100.0);
        mockProduct.setTraceId("traceId");

        given(cachedProductServiceClient.getProductByUniqueCode(anyString())).willReturn(Mono.error(new RuntimeException("Cache error")));
        given(externalProductServiceClient.getProductByUniqueCode(anyString())).willReturn(Mono.just(mockProduct));

        Mono<ProductDTO> result = productServiceClientContext.getProductByUniqueCode("encryptedCode")
                .contextWrite(ctx -> ctx.put("traceId", "traceId"));

        StepVerifier.create(result)
                .expectNext(mockProduct)
                .verifyComplete();
    }

    @Test
    void getProductByUniqueCode_shouldReturnErrorWhenAllStrategiesFail() {
        given(cachedProductServiceClient.getProductByUniqueCode(anyString())).willReturn(Mono.error(new RuntimeException("Cache error")));
        given(externalProductServiceClient.getProductByUniqueCode(anyString())).willReturn(Mono.error(new RuntimeException("External service error")));

        Mono<ProductDTO> result = productServiceClientContext.getProductByUniqueCode("encryptedCode")
                .contextWrite(ctx -> ctx.put("traceId", "traceId"));

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }
}
