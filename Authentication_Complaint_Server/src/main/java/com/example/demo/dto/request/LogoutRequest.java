package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LogoutRequest(
        @NotBlank(message = "이메일은 필수 입력값입니다")
        String memberEmail,

        @NotBlank(message = "액세스 토큰은 필수 입력값입니다")
        String accessToken
) {}