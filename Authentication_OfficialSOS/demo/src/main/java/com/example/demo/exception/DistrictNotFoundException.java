package com.example.demo.exception;

public class DistrictNotFoundException extends RuntimeException {
    public DistrictNotFoundException(String message) {
        super(message);
    }
}
