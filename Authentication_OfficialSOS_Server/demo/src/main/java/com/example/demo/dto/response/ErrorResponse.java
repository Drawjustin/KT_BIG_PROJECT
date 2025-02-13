package com.example.demo.dto.response;

import java.time.LocalDateTime;


public record ErrorResponse(
        String error,
        String message,
        LocalDateTime timestamp
) {
    public static ErrorResponse of(String error, String message) {
        return new ErrorResponse(error, message, LocalDateTime.now());
    }
}