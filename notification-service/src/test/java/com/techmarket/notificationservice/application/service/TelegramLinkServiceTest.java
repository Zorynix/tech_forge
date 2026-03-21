package com.techmarket.notificationservice.application.service;

import com.techmarket.notificationservice.application.port.out.TelegramSubscriptionRepository;
import com.techmarket.notificationservice.application.port.out.UserServiceClient;
import com.techmarket.notificationservice.domain.model.TelegramSubscription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelegramLinkServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;
    @Mock
    private ValueOperations<String, String> valueOperations;
    @Mock
    private TelegramSubscriptionRepository subscriptionRepository;
    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private TelegramLinkService telegramLinkService;

    @Test
    @DisplayName("generateCode should store code in Redis and return 6-digit code")
    void generateCode_storesInRedis() {
        UUID userId = UUID.randomUUID();
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        String code = telegramLinkService.generateCode(userId);

        assertThat(code).hasSize(6).containsOnlyDigits();
        verify(valueOperations).set(eq("tg:link:" + code), eq(userId.toString()), eq(Duration.ofMinutes(10)));
    }

    @Test
    @DisplayName("confirmCode should create new subscription when valid code")
    void confirmCode_validCode_createsSubscription() {
        UUID userId = UUID.randomUUID();
        Long chatId = 123456789L;
        String code = "123456";

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("tg:link:" + code)).thenReturn(userId.toString());
        when(subscriptionRepository.findByUserId(userId)).thenReturn(Optional.empty());

        UserServiceClient.UserInfo userInfo = new UserServiceClient.UserInfo(
                userId, "+79001234567", null, "Test", null);
        when(userServiceClient.getUserById(userId)).thenReturn(userInfo);

        UUID result = telegramLinkService.confirmCode(code, chatId);

        assertThat(result).isEqualTo(userId);
        verify(redisTemplate).delete("tg:link:" + code);
        verify(subscriptionRepository).save(any(TelegramSubscription.class));
        verify(userServiceClient).updateTelegramChatId(userId, chatId);
    }

    @Test
    @DisplayName("confirmCode should return null for invalid code")
    void confirmCode_invalidCode_returnsNull() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("tg:link:000000")).thenReturn(null);

        UUID result = telegramLinkService.confirmCode("000000", 123456789L);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("confirmCode should update existing subscription")
    void confirmCode_existingSubscription_updatesIt() {
        UUID userId = UUID.randomUUID();
        Long chatId = 987654321L;
        String code = "654321";

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("tg:link:" + code)).thenReturn(userId.toString());

        TelegramSubscription existing = TelegramSubscription.createNew("+79001234567", 111111L);
        existing.setUserId(userId);
        when(subscriptionRepository.findByUserId(userId)).thenReturn(Optional.of(existing));
        when(userServiceClient.getUserById(userId)).thenReturn(null);

        UUID result = telegramLinkService.confirmCode(code, chatId);

        assertThat(result).isEqualTo(userId);
        assertThat(existing.getChatId()).isEqualTo(chatId);
        verify(subscriptionRepository).save(existing);
    }
}
