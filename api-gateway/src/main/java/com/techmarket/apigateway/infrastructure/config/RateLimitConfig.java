package com.techmarket.apigateway.infrastructure.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Configuration
public class RateLimitConfig {

    /**
     * Rate limiter backed by Redis/Dragonfly.
     * replenishRate: 20 tokens per second.
     * burstCapacity: 40 tokens to allow short bursts.
     * requestedTokens: 1 token consumed per request.
     */
    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(20, 40, 1);
    }

    /**
     * Resolves the rate-limit key from the client IP address.
     * Falls back to "anonymous" when the remote address is unavailable.
     */
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            String ip = Objects.requireNonNull(exchange.getRequest().getRemoteAddress())
                    .getAddress()
                    .getHostAddress();
            return Mono.just(ip != null ? ip : "anonymous");
        };
    }
}
