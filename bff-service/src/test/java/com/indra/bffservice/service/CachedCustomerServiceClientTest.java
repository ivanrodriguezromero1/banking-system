package com.indra.bffservice.service;

import com.indra.bffservice.event.CustomerEvent;
import com.indra.bffservice.model.CustomerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class CachedCustomerServiceClientTest {

    @Mock
    private ReactiveRedisTemplate<String, Object> redisTemplate;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private CachedCustomerServiceClient cachedCustomerServiceClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCustomerByUniqueCode_shouldReturnCustomerAndPublishEvent() {
        CustomerDTO mockCustomer = new CustomerDTO("1", "Ivan", "Rodriguez", "DNI", "46794348", "traceId");

        given(redisTemplate.opsForValue().get(anyString())).willReturn(Mono.just(mockCustomer));

        Mono<CustomerDTO> result = cachedCustomerServiceClient.getCustomerByUniqueCode("encryptedCode")
                .contextWrite(ctx -> ctx.put("traceId", "traceId"));

        StepVerifier.create(result)
                .expectNext(mockCustomer)
                .verifyComplete();

        verify(eventPublisher, times(1)).publishEvent(any(CustomerEvent.class));
    }

    @Test
    void getCustomerByUniqueCode_shouldReturnErrorWhenCustomerNotFound() {
        given(redisTemplate.opsForValue().get(anyString())).willReturn(Mono.empty());

        Mono<CustomerDTO> result = cachedCustomerServiceClient.getCustomerByUniqueCode("encryptedCode")
                .contextWrite(ctx -> ctx.put("traceId", "traceId"));

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }
}
