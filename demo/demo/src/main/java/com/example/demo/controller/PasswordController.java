package com.example.demo.controller;

import com.example.demo.service.PasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class PasswordController {
    private final PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PostMapping("/password/reset")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("userEmail");
        try {
            passwordService.sendTemporaryPassword(email);
            return ResponseEntity.ok().body("임시 비밀번호가 이메일로 발송되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("비밀번호 재설정 실패");
        }
    }

    @PostMapping("/password/change")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request) {
        String email = request.get("userEmail");
        String newPassword = request.get("newPassword");
        try {
            passwordService.updatePassword(email, newPassword);
            return ResponseEntity.ok().body("비밀번호가 변경되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("비밀번호 변경 실패");
        }
    }
}