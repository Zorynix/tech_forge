package com.techmarket.notificationservice.application.port.in;

import java.util.UUID;

public interface ConfirmLinkCodeUseCase {

    UUID confirmCode(String code, Long chatId);
}
