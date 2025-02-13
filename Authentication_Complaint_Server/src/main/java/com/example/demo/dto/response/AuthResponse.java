package com.example.demo.dto.response;

import java.time.LocalDateTime;

public record AuthResponse(
        String memberEmail,
        String accessToken,
        String refreshToken
) {
    public static AuthResponse of(String memberEmail, String accessToken, String refreshToken, Long accessTokenExpiration) {
        LocalDateTime now = LocalDateTime.now();
        return new AuthResponse(
                memberEmail,
                accessToken,
                refreshToken
        );
    }
}
