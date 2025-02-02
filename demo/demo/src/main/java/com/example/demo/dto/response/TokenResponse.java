package com.example.demo.dto.response;

import java.time.LocalDateTime;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
    public static TokenResponse of(String accessToken, String refreshToken, Long accessTokenExpiration) {
        LocalDateTime now = LocalDateTime.now();
        return new TokenResponse(
                accessToken,
                refreshToken
        );
    }
}