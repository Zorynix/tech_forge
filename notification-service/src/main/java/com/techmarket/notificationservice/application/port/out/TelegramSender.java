package com.techmarket.notificationservice.application.port.out;

public interface TelegramSender {

    boolean sendMessage(Long chatId, String message);
}
