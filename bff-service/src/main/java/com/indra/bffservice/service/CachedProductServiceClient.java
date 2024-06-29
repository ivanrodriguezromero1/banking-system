package com.indra.bffservice.service;

import com.indra.bffservice.event.ProductEvent;
import com.indra.bffservice.model.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CachedProductServiceClient implements IProductServiceClient {

    private final ReactiveRedisTemplate<String, Object> redisTemplate;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public CachedProductServiceClient(ReactiveRedisTemplate<String, Object> redisTemplate, ApplicationEventPublisher eventPublisher) {
        this.redisTemplate = redisTemplate;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Mono<ProductDTO> getProductByUniqueCode(String uniqueCode) {
        return Mono.deferContextual(ctx -> {
            String traceId = ctx.getOrDefault("traceId", "");
            return redisTemplate.opsForValue()
                    .get(uniqueCode)
                    .cast(ProductDTO.class)
                    .doOnNext(productDTO -> {
                        productDTO.setTraceId(traceId);
                        eventPublisher.publishEvent(new ProductEvent(traceId, productDTO));
                    })
                    .switchIfEmpty(Mono.error(new RuntimeException("Product not found in cache")));
        });
    }
}
