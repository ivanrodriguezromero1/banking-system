package com.indra.bffservice.service;

import com.indra.bffservice.event.ProductEvent;
import com.indra.bffservice.model.ProductDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ExternalProductServiceClient implements IProductServiceClient {

    private final WebClient webClient;
    private final ApplicationEventPublisher eventPublisher;

    public ExternalProductServiceClient(@Value("${product.service.url}") String productServiceUrl, ApplicationEventPublisher eventPublisher) {
        this.webClient = WebClient.builder().baseUrl(productServiceUrl).build();
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Mono<ProductDTO> getProductByUniqueCode(String uniqueCode) {
        return Mono.deferContextual(ctx -> {
            String traceId = ctx.getOrDefault("traceId", "");
            return webClient.get()
                    .uri("/{uniqueCode}", uniqueCode)
                    .header("traceId", traceId)
                    .retrieve()
                    .bodyToMono(ProductDTO.class)
                    .doOnNext(productDTO -> eventPublisher.publishEvent(new ProductEvent(traceId, productDTO)));
        });
    }
}
