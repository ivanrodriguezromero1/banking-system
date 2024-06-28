package com.indra.bffservice.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Order(-2)
public class GlobalErrorWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange)
                .onErrorResume(throwable -> {
                    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
                    String errorCode = "INTERNAL_SERVER_ERROR";
                    String message = throwable.getMessage();

                    if (throwable instanceof ResponseStatusException) {
                        status = ((ResponseStatusException) throwable).getStatus();
                        errorCode = status.getReasonPhrase();
                    } else if (throwable instanceof CustomException) {
                        status = HttpStatus.BAD_REQUEST;
                        errorCode = ((CustomException) throwable).getErrorCode();
                    }

                    ErrorResponse errorResponse = new ErrorResponse(errorCode, message);

                    exchange.getResponse().setStatusCode(status);
                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                    log.error("Error: {}", errorResponse, throwable);

                    return exchange.getResponse().writeWith(
                            Mono.just(exchange.getResponse().bufferFactory().wrap(errorResponse.toString().getBytes()))
                    );
                });
    }
}
