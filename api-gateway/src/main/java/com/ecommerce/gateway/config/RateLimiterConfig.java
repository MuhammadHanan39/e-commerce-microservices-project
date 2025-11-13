package com.ecommerce.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfig {

    @Bean
    @Primary
    public KeyResolver userKeyResolver() {
        // Rate limit by user (when authentication is implemented)
        return exchange -> Mono.just(
                exchange.getRequest()
                        .getHeaders()
                        .getFirst("X-User-Id") != null ?
                        exchange.getRequest().getHeaders().getFirst("X-User-Id") :
                        "anonymous"
        );
    }

    @Bean
    public KeyResolver apiKeyResolver() {
        // Rate limit by API path
        return exchange -> Mono.just(
                exchange.getRequest().getPath().value()
        );
    }

    @Bean
    public KeyResolver ipKeyResolver() {
        // Rate limit by IP address
        return exchange -> Mono.just(
                exchange.getRequest()
                        .getRemoteAddress()
                        .getAddress()
                        .getHostAddress()
        );
    }
}