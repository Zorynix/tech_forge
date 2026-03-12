package com.techmarket.notificationservice.application.service;

import com.techmarket.notificationservice.application.port.in.ConfirmLinkCodeUseCase;
import com.techmarket.notificationservice.application.port.in.GenerateLinkCodeUseCase;
import com.techmarket.notificationservice.application.port.out.TelegramSubscriptionRepository;
import com.techmarket.notificationservice.application.port.out.UserServiceClient;
import com.techmarket.notificationservice.domain.model.TelegramSubscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramLinkService implements GenerateLinkCodeUseCase, ConfirmLinkCodeUseCase {

    private static final String KEY_PREFIX = "tg:link:";
    private static final Duration CODE_TTL = Duration.ofMinutes(10);
    private static final int CODE_LENGTH = 6;

    private final StringRedisTemplate redisTemplate;
    private final TelegramSubscriptionRepository subscriptionRepository;
    private final UserServiceClient userServiceClient;
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String generateCode(UUID userId) {
        String code = String.format("%0" + CODE_LENGTH + "d",
                secureRandom.nextInt((int) Math.pow(10, CODE_LENGTH)));
        redisTemplate.opsForValue().set(KEY_PREFIX + code, userId.toString(), CODE_TTL);
        log.info("Generated Telegram link code for userId={}: {}", userId, code);
        return code;
    }

    @Override
    public UUID confirmCode(String code, Long chatId) {
        String userIdStr = redisTemplate.opsForValue().get(KEY_PREFIX + code);
        if (userIdStr == null) {
            log.warn("Invalid or expired Telegram link code: {}", code);
            return null;
        }

        redisTemplate.delete(KEY_PREFIX + code);
        UUID userId = UUID.fromString(userIdStr);

        // Get user phone from auth-service
        String phone = null;
        try {
            UserServiceClient.UserInfo userInfo = userServiceClient.getUserById(userId);
            if (userInfo != null) {
                phone = userInfo.phone();
            }
        } catch (Exception e) {
            log.warn("Could not fetch user info for userId={}: {}", userId, e.getMessage());
        }

        // Create or update subscription
        Optional<TelegramSubscription> existing = subscriptionRepository.findByUserId(userId);
        if (existing.isPresent()) {
            TelegramSubscription sub = existing.get();
            sub.setChatId(chatId);
            sub.setActive(true);
            if (phone != null) sub.setPhone(phone);
            subscriptionRepository.save(sub);
        } else {
            TelegramSubscription sub = TelegramSubscription.createNew(
                    phone != null ? phone : "unknown", chatId);
            sub.setUserId(userId);
            subscriptionRepository.save(sub);
        }

        // Update auth-service User.telegramChatId so profile reflects the change
        try {
            userServiceClient.updateTelegramChatId(userId, chatId);
        } catch (Exception e) {
            log.warn("Failed to sync telegramChatId to auth-service: {}", e.getMessage());
        }

        log.info("Telegram linked: userId={}, chatId={}", userId, chatId);
        return userId;
    }
}
