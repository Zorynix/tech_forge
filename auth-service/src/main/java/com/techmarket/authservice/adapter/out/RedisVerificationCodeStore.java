package com.techmarket.authservice.adapter.out;

import com.techmarket.authservice.application.port.out.VerificationCodeStore;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
public class RedisVerificationCodeStore implements VerificationCodeStore {

    private static final String KEY_PREFIX = "auth:verification:";

    private final StringRedisTemplate redisTemplate;

    public RedisVerificationCodeStore(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(String phone, String code, Duration ttl) {
        redisTemplate.opsForValue().set(key(phone), code, ttl);
    }

    @Override
    public Optional<String> find(String phone) {
        String code = redisTemplate.opsForValue().get(key(phone));
        return Optional.ofNullable(code);
    }

    @Override
    public void delete(String phone) {
        redisTemplate.delete(key(phone));
    }

    private String key(String phone) {
        return KEY_PREFIX + phone;
    }
}
