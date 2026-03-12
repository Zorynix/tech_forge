package com.techmarket.authservice.application.port.out;

import com.techmarket.authservice.domain.model.User;

import java.util.UUID;

public interface TokenProvider {

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    boolean validateToken(String token);

    UUID extractUserId(String token);

    String extractPhone(String token);
}
