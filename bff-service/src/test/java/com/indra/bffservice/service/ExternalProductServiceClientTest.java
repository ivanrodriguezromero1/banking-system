package com.indra.bffservice.service;

import com.indra.bffservice.event.ProductEvent;
import com.indra.bffservice.model.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class ExternalProductServiceClientTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ExternalProductServiceClient externalProductServiceClient;

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
        given(webClient.get()).willReturn(requestHeadersUriSpec);
        given(requestHeadersUriSpec.uri(anyString(), any(Object[].class))).willReturn(requestHeadersSpec);
        given(requestHeadersSpec.header(anyString(), anyString())).willReturn(requestHeadersSpec);
        given(requestHeadersSpec.retrieve()).willReturn(responseSpec);
        given(responseSpec.bodyToMono(ProductDTO.class)).willReturn(Mono.just(mockProduct));

        Mono<ProductDTO> result = externalProductServiceClient.getProductByUniqueCode("encryptedCode")
                .contextWrite(ctx -> ctx.put("traceId", "traceId"));

        StepVerifier.create(result)
                .expectNext(mockProduct)
                .verifyComplete();

        verify(eventPublisher, times(1)).publishEvent(any(ProductEvent.class));
    }

    @Test
    void getProductByUniqueCode_shouldReturnErrorWhenProductNotFound() {
        given(webClient.get()).willReturn(requestHeadersUriSpec);
        given(requestHeadersUriSpec.uri(anyString(), any(Object[].class))).willReturn(requestHeadersSpec);
        given(requestHeadersSpec.header(anyString(), anyString())).willReturn(requestHeadersSpec);
        given(requestHeadersSpec.retrieve()).willReturn(responseSpec);
        given(responseSpec.bodyToMono(ProductDTO.class)).willReturn(Mono.empty());

        Mono<ProductDTO> result = externalProductServiceClient.getProductByUniqueCode("encryptedCode")
                .contextWrite(ctx -> ctx.put("traceId", "traceId"));

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(eventPublisher, never()).publishEvent(any(ProductEvent.class));
    }
}
