package com.indra.customerservice.repository;

import com.indra.customerservice.model.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Long> {
    Mono<Customer> findByUniqueCode(String uniqueCode);
}
