package com.example.demo.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(Long memberSeq) {
        super("Member not found with ID: " + memberSeq);
    }
}