package com.indra.bffservice.service;

import com.indra.bffservice.model.CustomerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceClient {

    private final WebClient webClient;

    public CustomerServiceClient(@Value("${customer.service.url}") String customerServiceUrl) {
        this.webClient = WebClient.builder().baseUrl(customerServiceUrl).build();
    }

    public Mono<CustomerDTO> getCustomerByUniqueCode(String uniqueCode) {
        return Mono.deferContextual(ctx -> {
            String traceId = ctx.getOrDefault("traceId", "");
            return webClient.get()
                    .uri("/{uniqueCode}", uniqueCode)
                    .header("traceId", traceId)
                    .retrieve()
                    .bodyToMono(CustomerDTO.class);
        });
    }
}
