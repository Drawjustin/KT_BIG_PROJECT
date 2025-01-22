package com.example.demo.controller;

import com.example.demo.dto.JoinDTO;
import com.example.demo.service.JoinService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.exception.EmailAlreadyExistsException;

import java.util.Map;

@RestController // RESTful 컨트롤러 지정
@RequestMapping("/api") // API 엔드포인트 기본 경로 설정
public class JoinController {
    private final JoinService joinService;

    public JoinController(JoinService joinService) {

        this.joinService = joinService;
    }

    // 회원가입 엔드포인트
    @PostMapping("/join")
    public ResponseEntity<?> registerUser(@RequestBody @Valid JoinDTO joinDTO) {
        try {
            joinService.joinProcess(joinDTO);
            return ResponseEntity.status(HttpServletResponse.SC_CREATED)
                    .body(Map.of("message", "회원가입 성공", "userEmail", joinDTO.getUserEmail()));
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpServletResponse.SC_CONFLICT)
                    .body(Map.of("error", "이메일 중복", "message", e.getMessage()));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST)
                    .body(Map.of("error", "입력 오류", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "서버 오류", "message", "회원가입 중 오류가 발생했습니다."));
        }
    }




}
