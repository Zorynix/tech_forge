package com.techmarket.notificationservice.adapter.out.persistence;

import com.techmarket.notificationservice.domain.model.TelegramSubscription;
import org.springframework.stereotype.Component;

@Component
public class TelegramSubscriptionMapper {

    public TelegramSubscriptionJpaEntity toEntity(TelegramSubscription domain) {
        return TelegramSubscriptionJpaEntity.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .chatId(domain.getChatId())
                .phone(domain.getPhone())
                .active(domain.isActive())
                .createdAt(domain.getCreatedAt())
                .build();
    }

    public TelegramSubscription toDomain(TelegramSubscriptionJpaEntity entity) {
        return new TelegramSubscription(
                entity.getId(),
                entity.getUserId(),
                entity.getChatId(),
                entity.getPhone(),
                entity.isActive(),
                entity.getCreatedAt()
        );
    }
}
