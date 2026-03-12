package com.techmarket.productservice.adapter.out.persistence;

import com.techmarket.productservice.application.port.out.ProductCachePort;
import com.techmarket.productservice.domain.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductCacheAdapter implements ProductCachePort {

    private static final String CACHE_PREFIX = "product:";
    private static final Duration TTL = Duration.ofMinutes(10);

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Optional<Product> get(UUID productId) {
        try {
            Object cached = redisTemplate.opsForValue().get(CACHE_PREFIX + productId);
            if (cached instanceof Product product) {
                return Optional.of(product);
            }
            return Optional.empty();
        } catch (Exception e) {
            log.warn("Failed to get product from cache, evicting: {}", e.getMessage());
            try {
                redisTemplate.delete(CACHE_PREFIX + productId);
            } catch (Exception ignored) {
            }
            return Optional.empty();
        }
    }

    @Override
    public void put(Product product) {
        try {
            redisTemplate.opsForValue().set(CACHE_PREFIX + product.getId(), product, TTL);
        } catch (Exception e) {
            log.warn("Failed to put product in cache: {}", e.getMessage());
        }
    }

    @Override
    public void evict(UUID productId) {
        try {
            redisTemplate.delete(CACHE_PREFIX + productId);
        } catch (Exception e) {
            log.warn("Failed to evict product from cache: {}", e.getMessage());
        }
    }
}
