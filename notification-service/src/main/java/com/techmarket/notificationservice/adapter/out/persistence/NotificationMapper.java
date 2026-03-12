package com.techmarket.notificationservice.adapter.out.persistence;

import com.techmarket.notificationservice.domain.model.Notification;
import com.techmarket.notificationservice.domain.model.NotificationChannel;
import com.techmarket.notificationservice.domain.model.NotificationStatus;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationJpaEntity toEntity(Notification domain) {
        return NotificationJpaEntity.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .orderId(domain.getOrderId())
                .channel(domain.getChannel().name())
                .message(domain.getMessage())
                .status(domain.getStatus().name())
                .sentAt(domain.getSentAt())
                .createdAt(domain.getCreatedAt())
                .build();
    }

    public Notification toDomain(NotificationJpaEntity entity) {
        return new Notification(
                entity.getId(),
                entity.getUserId(),
                entity.getOrderId(),
                NotificationChannel.valueOf(entity.getChannel()),
                entity.getMessage(),
                NotificationStatus.valueOf(entity.getStatus()),
                entity.getSentAt(),
                entity.getCreatedAt()
        );
    }
}
