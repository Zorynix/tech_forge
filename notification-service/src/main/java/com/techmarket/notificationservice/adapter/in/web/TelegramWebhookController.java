package com.techmarket.notificationservice.adapter.in.web;

import com.techmarket.notificationservice.application.port.in.ConfirmLinkCodeUseCase;
import com.techmarket.notificationservice.application.port.out.TelegramSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/telegram/webhook")
@RequiredArgsConstructor
@Slf4j
public class TelegramWebhookController {

    private final ConfirmLinkCodeUseCase confirmLinkCodeUseCase;
    private final TelegramSender telegramSender;

    @PostMapping
    public ResponseEntity<Void> handleWebhook(@RequestBody Map<String, Object> update) {
        try {
            processUpdate(update);
        } catch (Exception e) {
            log.error("Error processing Telegram webhook update", e);
        }
        return ResponseEntity.ok().build();
    }

    @SuppressWarnings("unchecked")
    private void processUpdate(Map<String, Object> update) {
        if (!update.containsKey("message")) {
            return;
        }

        Map<String, Object> message = (Map<String, Object>) update.get("message");
        Map<String, Object> chat = (Map<String, Object>) message.get("chat");
        Long chatId = ((Number) chat.get("id")).longValue();
        String text = (String) message.get("text");

        if (text == null) {
            return;
        }

        if (text.startsWith("/start")) {
            handleStartCommand(chatId, text);
        } else if (text.equals("/unlink")) {
            telegramSender.sendMessage(chatId,
                    "Для отвязки Telegram перейдите в настройки профиля на сайте.");
        }
    }

    private void handleStartCommand(Long chatId, String text) {
        String[] parts = text.split("\\s+", 2);

        if (parts.length < 2 || parts[1].isBlank()) {
            telegramSender.sendMessage(chatId,
                    "Добро пожаловать в Tech Forge! 👋\n\n" +
                    "Для привязки аккаунта:\n" +
                    "1. Войдите на сайт Tech Forge\n" +
                    "2. Перейдите в профиль\n" +
                    "3. Нажмите «Привязать Telegram»\n" +
                    "4. Перейдите по сгенерированной ссылке\n\n" +
                    "После привязки вы будете получать уведомления о статусе заказов.");
            return;
        }

        String code = parts[1].trim();

        // Try linking by code
        UUID userId = confirmLinkCodeUseCase.confirmCode(code, chatId);
        if (userId != null) {
            telegramSender.sendMessage(chatId,
                    "✅ Telegram успешно привязан к вашему аккаунту Tech Forge!\n\n" +
                    "Теперь вы будете получать уведомления о статусе ваших заказов.");
        } else {
            telegramSender.sendMessage(chatId,
                    "❌ Код недействителен или истёк.\n\n" +
                    "Перейдите в профиль на сайте и сгенерируйте новый код привязки.");
        }
    }
}
