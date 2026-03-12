package com.techmarket.authservice.application.port.out;

public interface EmailSender {
    void sendVerificationCode(String to, String code);
}
