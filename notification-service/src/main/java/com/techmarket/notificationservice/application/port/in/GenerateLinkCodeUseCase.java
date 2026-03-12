package com.techmarket.notificationservice.application.port.in;

import java.util.UUID;

public interface GenerateLinkCodeUseCase {

    String generateCode(UUID userId);
}
