package com.techmarket.authservice.adapter.in.web.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        long expiresIn
) {
}
