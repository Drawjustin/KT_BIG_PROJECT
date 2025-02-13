package com.example.demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email(message = "올바른 이메일 형식이 아닙니다")
        String memberEmail,

        @NotBlank(message = "비밀번호는 필수 입력값입니다")
        String memberPassword
) {}

