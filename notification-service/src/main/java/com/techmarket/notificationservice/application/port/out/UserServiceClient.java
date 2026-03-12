package com.techmarket.notificationservice.application.port.out;

import java.util.UUID;

public interface UserServiceClient {

    UserInfo getUserById(UUID userId);

    void updateTelegramChatId(UUID userId, Long chatId);

    record UserInfo(UUID id, String phone, String email, String name, Long telegramChatId) {
    }
}
