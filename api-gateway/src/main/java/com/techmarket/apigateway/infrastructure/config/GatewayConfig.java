package com.techmarket.apigateway.infrastructure.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final RedisRateLimiter redisRateLimiter;
    private final KeyResolver ipKeyResolver;

    public GatewayConfig(RedisRateLimiter redisRateLimiter, KeyResolver ipKeyResolver) {
        this.redisRateLimiter = redisRateLimiter;
        this.ipKeyResolver = ipKeyResolver;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()

                // Product Service routes
                .route("product-service-products", r -> r
                        .path("/api/v1/products/**")
                        .filters(f -> f
                                .circuitBreaker(cb -> cb
                                        .setName("productServiceCB")
                                        .setFallbackUri("forward:/fallback/product-service"))
                                .requestRateLimiter(rl -> rl
                                        .setRateLimiter(redisRateLimiter)
                                        .setKeyResolver(ipKeyResolver))
                                .retry(retryConfig -> retryConfig.setRetries(2)))
                        .uri("http://localhost:8081"))

                .route("product-service-categories", r -> r
                        .path("/api/v1/categories/**")
                        .filters(f -> f
                                .circuitBreaker(cb -> cb
                                        .setName("productServiceCB")
                                        .setFallbackUri("forward:/fallback/product-service"))
                                .requestRateLimiter(rl -> rl
                                        .setRateLimiter(redisRateLimiter)
                                        .setKeyResolver(ipKeyResolver))
                                .retry(retryConfig -> retryConfig.setRetries(2)))
                        .uri("http://localhost:8081"))

                // Auth Service routes
                .route("auth-service-auth", r -> r
                        .path("/api/v1/auth/**")
                        .filters(f -> f
                                .circuitBreaker(cb -> cb
                                        .setName("authServiceCB")
                                        .setFallbackUri("forward:/fallback/auth-service"))
                                .requestRateLimiter(rl -> rl
                                        .setRateLimiter(redisRateLimiter)
                                        .setKeyResolver(ipKeyResolver))
                                .retry(retryConfig -> retryConfig.setRetries(2)))
                        .uri("http://localhost:8082"))

                .route("auth-service-users", r -> r
                        .path("/api/v1/users/**")
                        .filters(f -> f
                                .circuitBreaker(cb -> cb
                                        .setName("authServiceCB")
                                        .setFallbackUri("forward:/fallback/auth-service"))
                                .requestRateLimiter(rl -> rl
                                        .setRateLimiter(redisRateLimiter)
                                        .setKeyResolver(ipKeyResolver))
                                .retry(retryConfig -> retryConfig.setRetries(2)))
                        .uri("http://localhost:8082"))

                // Order Service routes
                .route("order-service-cart", r -> r
                        .path("/api/v1/cart/**")
                        .filters(f -> f
                                .circuitBreaker(cb -> cb
                                        .setName("orderServiceCB")
                                        .setFallbackUri("forward:/fallback/order-service"))
                                .requestRateLimiter(rl -> rl
                                        .setRateLimiter(redisRateLimiter)
                                        .setKeyResolver(ipKeyResolver))
                                .retry(retryConfig -> retryConfig.setRetries(2)))
                        .uri("http://localhost:8083"))

                .route("order-service-orders", r -> r
                        .path("/api/v1/orders/**")
                        .filters(f -> f
                                .circuitBreaker(cb -> cb
                                        .setName("orderServiceCB")
                                        .setFallbackUri("forward:/fallback/order-service"))
                                .requestRateLimiter(rl -> rl
                                        .setRateLimiter(redisRateLimiter)
                                        .setKeyResolver(ipKeyResolver))
                                .retry(retryConfig -> retryConfig.setRetries(2)))
                        .uri("http://localhost:8083"))

                // Notification Service routes
                .route("notification-service-telegram", r -> r
                        .path("/api/v1/telegram/**")
                        .filters(f -> f
                                .circuitBreaker(cb -> cb
                                        .setName("notificationServiceCB")
                                        .setFallbackUri("forward:/fallback/notification-service"))
                                .requestRateLimiter(rl -> rl
                                        .setRateLimiter(redisRateLimiter)
                                        .setKeyResolver(ipKeyResolver)))
                        .uri("http://localhost:8084"))

                .route("notification-service", r -> r
                        .path("/api/v1/notifications/**")
                        .filters(f -> f
                                .circuitBreaker(cb -> cb
                                        .setName("notificationServiceCB")
                                        .setFallbackUri("forward:/fallback/notification-service"))
                                .requestRateLimiter(rl -> rl
                                        .setRateLimiter(redisRateLimiter)
                                        .setKeyResolver(ipKeyResolver))
                                .retry(retryConfig -> retryConfig.setRetries(2)))
                        .uri("http://localhost:8084"))

                .build();
    }
}
