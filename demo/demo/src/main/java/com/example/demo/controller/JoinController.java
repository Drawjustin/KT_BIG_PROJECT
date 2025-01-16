package com.example.demo.controller;

import com.example.demo.dto.JoinDTO;
import com.example.demo.service.JoinService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // RESTful 컨트롤러 지정
@RequestMapping("/api") // API 엔드포인트 기본 경로 설정
public class JoinController {
    private final JoinService joinService;

    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    // 회원가입 엔드포인트
    @PostMapping("/join")
    public ResponseEntity<String> registerUser(@RequestBody JoinDTO joinDTO) {
        try {
            // 회원가입 처리
            joinService.joinProcess(joinDTO);
            // 성공 응답 반환
            return ResponseEntity.status(201).body("회원가입 성공");
        } catch (IllegalArgumentException e) {
            // 이미 존재하는 이메일일 경우
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            // 기타 서버 오류
            return ResponseEntity.status(500).body("에러: " + e.getMessage());
        }
    }


}
