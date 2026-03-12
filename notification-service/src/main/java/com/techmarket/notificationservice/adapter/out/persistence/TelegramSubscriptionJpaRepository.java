package com.techmarket.notificationservice.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TelegramSubscriptionJpaRepository extends JpaRepository<TelegramSubscriptionJpaEntity, UUID> {

    Optional<TelegramSubscriptionJpaEntity> findByUserId(UUID userId);

    Optional<TelegramSubscriptionJpaEntity> findByPhone(String phone);

    Optional<TelegramSubscriptionJpaEntity> findByChatId(Long chatId);
}
