package com.example.demo.utils;

import java.io.IOException;
import java.util.function.Supplier;

public class ExceptionHandlerUtil {
    @FunctionalInterface
    public interface CheckedExceptionSupplier<T> {
        T get() throws IOException;
    }

    // 일반 예외용
    public static <T> T executeWithException(Supplier<T> action, RuntimeException customException) {
        try {
            return action.get();
        } catch (Exception e) {
            throw customException;
        }
    }

    // IOException용
    public static <T> T executeWithIOException(CheckedExceptionSupplier<T> action, RuntimeException customException) {
        try {
            return action.get();
        } catch (Exception e) {
            throw customException;
        }
    }
}
