package com.indra.bffservice.config;

import com.indra.bffservice.interceptor.LoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${product.service.url}")
    private String productServiceUrl;

    @Value("${customer.service.url}")
    private String customerServiceUrl;

    @Bean
    public WebClient productServiceWebClient() {
        return WebClient.builder()
                .baseUrl(productServiceUrl)
                .filter(LoggingInterceptor.logRequest())
                .filter(LoggingInterceptor.logResponse())
                .build();
    }

    @Bean
    public WebClient customerServiceWebClient() {
        return WebClient.builder()
                .baseUrl(customerServiceUrl)
                .filter(LoggingInterceptor.logRequest())
                .filter(LoggingInterceptor.logResponse())
                .build();
    }
}
