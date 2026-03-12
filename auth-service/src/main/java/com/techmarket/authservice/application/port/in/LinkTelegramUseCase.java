package com.techmarket.authservice.application.port.in;

import java.util.UUID;

public interface LinkTelegramUseCase {

    void linkTelegram(UUID userId, Long chatId);
}
