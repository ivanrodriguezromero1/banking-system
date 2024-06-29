package com.indra.bffservice.service;

import com.indra.bffservice.event.CustomerEvent;
import com.indra.bffservice.model.CustomerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class CustomerServiceClientTest {

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
    private CustomerServiceClient customerServiceClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerServiceClient = new CustomerServiceClient("http://localhost:8081", eventPublisher);
    }

    @Test
    void getCustomerByUniqueCode_shouldReturnCustomerAndPublishEvent() {
        CustomerDTO mockCustomer = new CustomerDTO("1", "Ivan", "Rodriguez", "DNI", "46794348", "traceId");

        given(webClient.get()).willReturn(requestHeadersUriSpec);
        given(requestHeadersUriSpec.uri("/{uniqueCode}", "encryptedCode")).willReturn(requestHeadersSpec);
        given(requestHeadersSpec.header(anyString(), anyString())).willReturn(requestHeadersSpec);
        given(requestHeadersSpec.retrieve()).willReturn(responseSpec);
        given(responseSpec.bodyToMono(CustomerDTO.class)).willReturn(Mono.just(mockCustomer));

        Mono<CustomerDTO> result = customerServiceClient.getCustomerByUniqueCode("encryptedCode")
                .contextWrite(ctx -> ctx.put("traceId", "traceId"));

        StepVerifier.create(result)
                .expectNext(mockCustomer)
                .verifyComplete();

        verify(eventPublisher, times(1)).publishEvent(any(CustomerEvent.class));
    }

    @Test
    void getCustomerByUniqueCode_shouldReturnErrorWhenCustomerNotFound() {
        given(webClient.get()).willReturn(requestHeadersUriSpec);
        given(requestHeadersUriSpec.uri("/{uniqueCode}", "encryptedCode")).willReturn(requestHeadersSpec);
        given(requestHeadersSpec.header(anyString(), anyString())).willReturn(requestHeadersSpec);
        given(requestHeadersSpec.retrieve()).willReturn(responseSpec);
        given(responseSpec.bodyToMono(CustomerDTO.class)).willReturn(Mono.empty());

        Mono<CustomerDTO> result = customerServiceClient.getCustomerByUniqueCode("encryptedCode")
                .contextWrite(ctx -> ctx.put("traceId", "traceId"));

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }
}
