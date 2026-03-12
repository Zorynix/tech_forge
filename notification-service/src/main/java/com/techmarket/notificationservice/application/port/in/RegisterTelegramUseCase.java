package com.techmarket.notificationservice.application.port.in;

public interface RegisterTelegramUseCase {

    void register(String phone, Long chatId);
}
