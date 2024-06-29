package com.indra.bffservice.service;

import com.indra.bffservice.model.CustomerDTO;
import reactor.core.publisher.Mono;

public interface ICustomerServiceClient {
    Mono<CustomerDTO> getCustomerByUniqueCode(String uniqueCode);
}
