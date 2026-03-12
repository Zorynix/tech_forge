package com.techmarket.notificationservice.application.port.out;

import com.techmarket.notificationservice.domain.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface NotificationRepository {

    Notification save(Notification notification);

    Page<Notification> findByUserId(UUID userId, Pageable pageable);
}
