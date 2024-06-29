package com.indra.bffservice.service;

import com.indra.bffservice.event.ProductEvent;
import com.indra.bffservice.model.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CachedProductServiceClientTest {

    @Mock
    private ReactiveRedisTemplate<String, Object> redisTemplate;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private CachedProductServiceClient cachedProductServiceClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProductByUniqueCode_shouldReturnProductAndPublishEvent() {
        ProductDTO mockProduct = new ProductDTO();
        mockProduct.setId("1");
        mockProduct.setName("Product A");
        mockProduct.setType("Type A");
        mockProduct.setBalance( 100.0);
        mockProduct.setTraceId("traceId");
        given(redisTemplate.opsForValue().get(anyString())).willReturn(Mono.just(mockProduct));

        Mono<ProductDTO> result = cachedProductServiceClient.getProductByUniqueCode("encryptedCode")
                .contextWrite(ctx -> ctx.put("traceId", "traceId"));

        StepVerifier.create(result)
                .expectNext(mockProduct)
                .verifyComplete();

        verify(eventPublisher, times(1)).publishEvent(any(ProductEvent.class));
    }

    @Test
    void getProductByUniqueCode_shouldReturnErrorWhenProductNotFound() {
        given(redisTemplate.opsForValue().get(anyString())).willReturn(Mono.empty());

        Mono<ProductDTO> result = cachedProductServiceClient.getProductByUniqueCode("encryptedCode")
                .contextWrite(ctx -> ctx.put("traceId", "traceId"));

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }
}
