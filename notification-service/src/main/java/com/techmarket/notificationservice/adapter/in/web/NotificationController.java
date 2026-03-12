package com.techmarket.notificationservice.adapter.in.web;

import com.techmarket.notificationservice.adapter.in.web.dto.NotificationResponse;
import com.techmarket.notificationservice.adapter.in.web.dto.PageResponse;
import com.techmarket.notificationservice.application.port.in.GetNotificationHistoryUseCase;
import com.techmarket.notificationservice.infrastructure.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final GetNotificationHistoryUseCase getNotificationHistoryUseCase;

    @GetMapping
    public ResponseEntity<PageResponse<NotificationResponse>> getNotificationHistory(
            @AuthenticationPrincipal UserPrincipal principal,
            @PageableDefault(size = 20) Pageable pageable) {
        var page = getNotificationHistoryUseCase.getByUserId(principal.getUserId(), pageable);
        return ResponseEntity.ok(PageResponse.from(page, NotificationResponse::from));
    }
}
