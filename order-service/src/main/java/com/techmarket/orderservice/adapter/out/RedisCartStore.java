package com.techmarket.orderservice.adapter.out;

import com.techmarket.orderservice.application.port.out.CartStore;
import com.techmarket.orderservice.domain.model.Cart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisCartStore implements CartStore {

    private static final String KEY_PREFIX = "cart:";

    private final RedisTemplate<String, Cart> redisTemplate;

    @Value("${app.cart.ttl-days:7}")
    private int ttlDays;

    @Override
    public Cart get(UUID userId) {
        try {
            return redisTemplate.opsForValue().get(key(userId));
        } catch (Exception e) {
            log.warn("Failed to deserialize cart for user {}, evicting corrupted entry", userId, e);
            redisTemplate.delete(key(userId));
            return null;
        }
    }

    @Override
    public void save(UUID userId, Cart cart) {
        redisTemplate.opsForValue().set(key(userId), cart, Duration.ofDays(ttlDays));
    }

    @Override
    public void delete(UUID userId) {
        redisTemplate.delete(key(userId));
    }

    private String key(UUID userId) {
        return KEY_PREFIX + userId.toString();
    }
}
