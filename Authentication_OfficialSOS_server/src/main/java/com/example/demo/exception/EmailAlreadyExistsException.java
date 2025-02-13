package com.example.demo.exception;

public class EmailAlreadyExistsException extends AuthException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}