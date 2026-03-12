package com.techmarket.notificationservice.adapter.out.persistence;

import com.techmarket.notificationservice.application.port.out.TelegramSubscriptionRepository;
import com.techmarket.notificationservice.domain.model.TelegramSubscription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TelegramSubscriptionPersistenceAdapter implements TelegramSubscriptionRepository {

    private final TelegramSubscriptionJpaRepository jpaRepository;
    private final TelegramSubscriptionMapper mapper;

    @Override
    public Optional<TelegramSubscription> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).map(mapper::toDomain);
    }

    @Override
    public Optional<TelegramSubscription> findByPhone(String phone) {
        return jpaRepository.findByPhone(phone).map(mapper::toDomain);
    }

    @Override
    public Optional<TelegramSubscription> findByChatId(Long chatId) {
        return jpaRepository.findByChatId(chatId).map(mapper::toDomain);
    }

    @Override
    public TelegramSubscription save(TelegramSubscription subscription) {
        TelegramSubscriptionJpaEntity entity = mapper.toEntity(subscription);
        TelegramSubscriptionJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
