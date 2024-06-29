package com.indra.bffservice.interceptor;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@Component
public class LoggingInterceptor {

    private static final Logger LOGGER = Logger.getLogger(LoggingInterceptor.class.getName());

    public static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            LOGGER.info("Request: " + clientRequest.method() + " " + clientRequest.url());
            HttpHeaders headers = clientRequest.headers();
            headers.forEach((name, values) -> values.forEach(value -> LOGGER.info(name + ": " + value)));
            return Mono.just(clientRequest);
        });
    }

    public static ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            LOGGER.info("Response Status Code: " + clientResponse.statusCode());
            HttpHeaders headers = clientResponse.headers().asHttpHeaders();
            headers.forEach((name, values) -> values.forEach(value -> LOGGER.info(name + ": " + value)));
            return Mono.just(clientResponse);
        });
    }
}
