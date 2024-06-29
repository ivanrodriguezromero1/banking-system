package com.indra.bffservice.listener;

import com.indra.bffservice.event.CustomerEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class CacheCustomerEventListener {

    private final ReactiveRedisTemplate<String, Object> redisTemplate;

    public CacheCustomerEventListener(ReactiveRedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @EventListener
    public void handleCustomerEvent(CustomerEvent event) {
        redisTemplate.opsForValue()
                .set(event.getCustomer().getId(), event.getCustomer())
                .subscribe();
    }
}
