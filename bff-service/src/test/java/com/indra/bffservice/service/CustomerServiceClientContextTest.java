package com.indra.bffservice.service;

import com.indra.bffservice.model.CustomerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

class CustomerServiceClientContextTest {

    @Mock
    private ICustomerServiceClient redisCustomerServiceClient;

    @Mock
    private ICustomerServiceClient postgreSQLCustomerServiceClient;

    @Mock
    private ICustomerServiceClient externalCustomerServiceClient;

    @InjectMocks
    private CustomerServiceClientContext customerServiceClientContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCustomerByUniqueCode_shouldReturnCustomerFromRedis() {
        CustomerDTO mockCustomer = new CustomerDTO("1", "Ivan", "Rodriguez", "DNI", "46794348", "traceId");

        given(redisCustomerServiceClient.getCustomerByUniqueCode(anyString())).willReturn(Mono.just(mockCustomer));

        Mono<CustomerDTO> result = customerServiceClientContext.getCustomerByUniqueCode("encryptedCode")
                .contextWrite(ctx -> ctx.put("traceId", "traceId"));

        StepVerifier.create(result)
                .expectNext(mockCustomer)
                .verifyComplete();
    }

    @Test
    void getCustomerByUniqueCode_shouldReturnCustomerFromPostgreSQLWhenRedisFails() {
        CustomerDTO mockCustomer = new CustomerDTO("1", "Ivan", "Rodriguez", "DNI", "46794348", "traceId");

        given(redisCustomerServiceClient.getCustomerByUniqueCode(anyString())).willReturn(Mono.error(new RuntimeException("Redis error")));
        given(postgreSQLCustomerServiceClient.getCustomerByUniqueCode(anyString())).willReturn(Mono.just(mockCustomer));

        Mono<CustomerDTO> result = customerServiceClientContext.getCustomerByUniqueCode("encryptedCode")
                .contextWrite(ctx -> ctx.put("traceId", "traceId"));

        StepVerifier.create(result)
                .expectNext(mockCustomer)
                .verifyComplete();
    }

    @Test
    void getCustomerByUniqueCode_shouldReturnCustomerFromExternalWhenRedisAndPostgreSQLFail() {
        CustomerDTO mockCustomer = new CustomerDTO("1", "Ivan", "Rodriguez", "DNI", "46794348", "traceId");

        given(redisCustomerServiceClient.getCustomerByUniqueCode(anyString())).willReturn(Mono.error(new RuntimeException("Redis error")));
        given(postgreSQLCustomerServiceClient.getCustomerByUniqueCode(anyString())).willReturn(Mono.error(new RuntimeException("PostgreSQL error")));
        given(externalCustomerServiceClient.getCustomerByUniqueCode(anyString())).willReturn(Mono.just(mockCustomer));

        Mono<CustomerDTO> result = customerServiceClientContext.getCustomerByUniqueCode("encryptedCode")
                .contextWrite(ctx -> ctx.put("traceId", "traceId"));

        StepVerifier.create(result)
                .expectNext(mockCustomer)
                .verifyComplete();
    }

    @Test
    void getCustomerByUniqueCode_shouldReturnErrorWhenAllStrategiesFail() {
        given(redisCustomerServiceClient.getCustomerByUniqueCode(anyString())).willReturn(Mono.error(new RuntimeException("Redis error")));
        given(postgreSQLCustomerServiceClient.getCustomerByUniqueCode(anyString())).willReturn(Mono.error(new RuntimeException("PostgreSQL error")));
        given(externalCustomerServiceClient.getCustomerByUniqueCode(anyString())).willReturn(Mono.error(new RuntimeException("External service error")));

        Mono<CustomerDTO> result = customerServiceClientContext.getCustomerByUniqueCode("encryptedCode")
                .contextWrite(ctx -> ctx.put("traceId", "traceId"));

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }
}
