package com.indra.bffservice.service;

import com.indra.bffservice.event.CustomerEvent;
import com.indra.bffservice.model.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CachedCustomerServiceClient implements ICustomerServiceClient {

    private final ReactiveRedisTemplate<String, Object> redisTemplate;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public CachedCustomerServiceClient(ReactiveRedisTemplate<String, Object> redisTemplate, ApplicationEventPublisher eventPublisher) {
        this.redisTemplate = redisTemplate;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Mono<CustomerDTO> getCustomerByUniqueCode(String uniqueCode) {
        return Mono.deferContextual(ctx -> {
            String traceId = ctx.getOrDefault("traceId", "");
            return redisTemplate.opsForValue()
                    .get(uniqueCode)
                    .cast(CustomerDTO.class)
                    .doOnNext(customerDTO -> {
                        customerDTO.setTraceId(traceId);
                        eventPublisher.publishEvent(new CustomerEvent(traceId, customerDTO));
                    })
                    .switchIfEmpty(Mono.error(new RuntimeException("Customer not found in cache")));
        });
    }
}
