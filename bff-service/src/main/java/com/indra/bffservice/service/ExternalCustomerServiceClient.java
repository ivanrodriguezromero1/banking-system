package com.indra.bffservice.service;

import com.indra.bffservice.event.CustomerEvent;
import com.indra.bffservice.model.CustomerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ExternalCustomerServiceClient implements ICustomerServiceClient {

    private final WebClient webClient;
    private final ApplicationEventPublisher eventPublisher;

    public ExternalCustomerServiceClient(@Value("${customer.service.url}") String customerServiceUrl, ApplicationEventPublisher eventPublisher) {
        this.webClient = WebClient.builder().baseUrl(customerServiceUrl).build();
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Mono<CustomerDTO> getCustomerByUniqueCode(String uniqueCode) {
        return Mono.deferContextual(ctx -> {
            String traceId = ctx.getOrDefault("traceId", "");
            return webClient.get()
                    .uri("/{uniqueCode}", uniqueCode)
                    .header("traceId", traceId)
                    .retrieve()
                    .bodyToMono(CustomerDTO.class)
                    .doOnNext(customerDTO -> eventPublisher.publishEvent(new CustomerEvent(traceId, customerDTO)));
        });
    }
}
