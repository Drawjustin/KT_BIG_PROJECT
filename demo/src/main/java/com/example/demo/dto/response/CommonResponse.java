package com.example.demo.dto.response;

public record CommonResponse<T>(
        String status,
        String message,
        T data
) {
    public static <T> CommonResponse<T> success(String message, T data) {
        return new CommonResponse<>("success", message, data);
    }

    public static <T> CommonResponse<T> error(String message) {
        return new CommonResponse<>("error", message, null);
    }
}