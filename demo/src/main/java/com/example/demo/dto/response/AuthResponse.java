package com.example.demo.dto.response;

import java.time.LocalDateTime;

public record AuthResponse(
        String userEmail,
        String accessToken,
        String refreshToken
) {
    public static AuthResponse of(String userEmail, String accessToken, String refreshToken, Long accessTokenExpiration) {
        LocalDateTime now = LocalDateTime.now();
        return new AuthResponse(
                userEmail,
                accessToken,
                refreshToken
        );
    }
}
