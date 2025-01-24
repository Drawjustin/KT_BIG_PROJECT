package com.example.demo.controller;

import com.example.demo.entity.RefreshEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.jwt.JWTUtil;
import com.example.demo.repository.RefreshRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AccessTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LogoutController {
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final AccessTokenService accessTokenService;
    private final UserRepository userRepository;

    public LogoutController(JWTUtil jwtUtil, RefreshRepository refreshRepository,AccessTokenService accessTokenService,UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
        this.accessTokenService=accessTokenService;
        this.userRepository=userRepository;
    }

    @PostMapping("/logout")
    @Transactional
    public ResponseEntity<String> logout(@RequestBody Map<String, String> request) {

        String userEmail = request.get("userEmail");
        // body에서 액세스토큰 추출
        String accessToken = request.get("access");

        // Redis에서 액세스 토큰 삭제
        accessTokenService.deleteAccessToken(userEmail);

        // DB에서 리프레시 토큰 삭제
        Optional<UserEntity> userOptional = userRepository.findByUserEmail(userEmail);
        if (userOptional.isPresent()) {
            refreshRepository.deleteByUserEntity(userOptional.get());
            return ResponseEntity.ok("로그아웃 성공");
        }


        return ResponseEntity.badRequest().body("사용자를 찾을 수 없습니다");
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}