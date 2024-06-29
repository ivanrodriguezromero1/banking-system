package com.indra.bffservice.service;

import com.indra.bffservice.model.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class ProductServiceClientTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private ProductServiceClient productServiceClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productServiceClient = new ProductServiceClient(webClient);
    }

    @Test
    void getProductsByCustomerId_shouldReturnProducts() {
        ProductDTO mockProduct = new ProductDTO("1", "Product A", "Type A", 100.0, "traceId");

        given(webClient.get()).willReturn(requestHeadersUriSpec);
        given(requestHeadersUriSpec.uri(anyString(), any(Object[].class))).willReturn(requestHeadersSpec);
        given(requestHeadersSpec.header(anyString(), anyString())).willReturn(requestHeadersSpec);
        given(requestHeadersSpec.retrieve()).willReturn(responseSpec);
        given(responseSpec.bodyToFlux(ProductDTO.class)).willReturn(Flux.just(mockProduct));

        Flux<ProductDTO> result = productServiceClient.getProductsByCustomerId("customerId")
                .contextWrite(ctx -> ctx.put("traceId", "traceId"));

        StepVerifier.create(result)
                .expectNext(mockProduct)
                .verifyComplete();
    }

    @Test
    void getProductsByCustomerId_shouldHandleError() {
        given(webClient.get()).willReturn(requestHeadersUriSpec);
        given(requestHeadersUriSpec.uri(anyString(), any(Object[].class))).willReturn(requestHeadersSpec);
        given(requestHeadersSpec.header(anyString(), anyString())).willReturn(requestHeadersSpec);
        given(requestHeadersSpec.retrieve()).willReturn(responseSpec);
        given(responseSpec.bodyToFlux(ProductDTO.class)).willReturn(Flux.error(new WebClientResponseException(500, "Internal Server Error", null, null, null)));

        Flux<ProductDTO> result = productServiceClient.getProductsByCustomerId("customerId")
                .contextWrite(ctx -> ctx.put("traceId", "traceId"));

        StepVerifier.create(result)
                .expectError(WebClientResponseException.class)
                .verify();
    }
}
