package com.example.demo.utils;

import java.util.function.Supplier;

public class ExceptionHandlerUtil {
    public static <T> T executeWithCustomException(Supplier<T> action, RuntimeException customException) {
        try {
            return action.get();
        } catch (Exception e) {
            throw customException; // 지정된 커스텀 예외 던지기
        }
    }

    public static void executeWithCustomException(Runnable action, RuntimeException customException) {
        try {
            action.run();
        } catch (Exception e) {
            throw customException; // 지정된 커스텀 예외 던지기
        }
    }
}
