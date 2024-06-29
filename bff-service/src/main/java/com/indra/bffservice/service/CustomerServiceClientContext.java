package com.indra.bffservice.service;

import com.indra.bffservice.model.CustomerDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceClientContext {

    private final ICustomerServiceClient redisCustomerServiceClient;
    private final ICustomerServiceClient externalCustomerServiceClient;

    public CustomerServiceClientContext(CachedCustomerServiceClient redisCustomerServiceClient,
                                        ExternalCustomerServiceClient externalCustomerServiceClient) {
        this.redisCustomerServiceClient = redisCustomerServiceClient;
        this.externalCustomerServiceClient = externalCustomerServiceClient;
    }

    public Mono<CustomerDTO> getCustomerByUniqueCode(String uniqueCode) {
        return redisCustomerServiceClient.getCustomerByUniqueCode(uniqueCode)
                .onErrorResume(ex -> externalCustomerServiceClient.getCustomerByUniqueCode(uniqueCode));
    }
}
