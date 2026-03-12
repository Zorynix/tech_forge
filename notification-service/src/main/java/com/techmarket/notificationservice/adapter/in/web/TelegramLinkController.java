package com.techmarket.notificationservice.adapter.in.web;

import com.techmarket.notificationservice.application.port.in.GenerateLinkCodeUseCase;
import com.techmarket.notificationservice.infrastructure.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/telegram/link")
@RequiredArgsConstructor
public class TelegramLinkController {

    private final GenerateLinkCodeUseCase generateLinkCodeUseCase;

    @Value("${app.telegram.bot-username}")
    private String botUsername;

    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateLinkCode(
            @AuthenticationPrincipal UserPrincipal principal) {
        String code = generateLinkCodeUseCase.generateCode(principal.getUserId());
        String botLink = "https://t.me/" + botUsername + "?start=" + code;
        return ResponseEntity.ok(Map.of(
                "code", code,
                "botLink", botLink
        ));
    }
}
