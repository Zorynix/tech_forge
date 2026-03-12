package com.techmarket.notificationservice.application.port.in;

import com.techmarket.notificationservice.domain.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface GetNotificationHistoryUseCase {

    Page<Notification> getByUserId(UUID userId, Pageable pageable);
}
