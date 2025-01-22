package com.example.demo.controller;

import com.example.demo.jwt.JWTUtil;
import com.example.demo.repository.RefreshRepository;
import com.example.demo.service.AccessTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LogoutController {
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final AccessTokenService accessTokenService;

    public LogoutController(JWTUtil jwtUtil, RefreshRepository refreshRepository,AccessTokenService accessTokenService) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
        this.accessTokenService=accessTokenService;
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키에서 리프레시 토큰 추출
        String refreshToken = extractRefreshTokenFromCookie(request);

        if (refreshToken != null) {
            try {
                // 토큰 유효성 검증
                if (!jwtUtil.isExpired(refreshToken) &&
                        "refresh".equals(jwtUtil.getCategory(refreshToken))) {

                    // 1. Redis의 access 토큰만 삭제
                    String accessToken = request.getHeader("access");
                    if (accessToken != null) {
                        accessTokenService.deleteAccessToken(accessToken);
                    }

                    // refresh 토큰은 삭제하지 않음 (재사용)
                    // refreshRepository.deleteByRefreshTokenContent(refreshToken);

                }
            } catch (Exception e) {
                // 토큰 검증 중 오류 발생
                return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST)
                        .body("로그아웃 처리 중 오류가 발생했습니다.");
            }
        }

        // 보안 컨텍스트 클리어
        SecurityContextHolder.clearContext();

//        // 리프레시 토큰 쿠키 제거
//        Cookie cookie = new Cookie("refresh", null);
//        cookie.setMaxAge(0);
//        cookie.setHttpOnly(true);
//        cookie.setPath("/");
//        response.addCookie(cookie);

        return ResponseEntity.ok("로그아웃 성공");
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