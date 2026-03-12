package com.techmarket.notificationservice.application.port.out;

import com.techmarket.notificationservice.domain.model.TelegramSubscription;

import java.util.Optional;
import java.util.UUID;

public interface TelegramSubscriptionRepository {

    Optional<TelegramSubscription> findByUserId(UUID userId);

    Optional<TelegramSubscription> findByPhone(String phone);

    Optional<TelegramSubscription> findByChatId(Long chatId);

    TelegramSubscription save(TelegramSubscription subscription);
}
