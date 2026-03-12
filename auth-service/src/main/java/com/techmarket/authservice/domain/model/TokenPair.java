package com.techmarket.authservice.domain.model;

public record TokenPair(
        String accessToken,
        String refreshToken,
        long expiresIn
) {
}
