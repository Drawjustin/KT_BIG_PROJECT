package com.example.demo.controller;

import com.example.demo.entity.RefreshEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.jwt.JWTUtil;
import com.example.demo.repository.RefreshRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AccessTokenService;
import com.example.demo.utils.CookieUtil;
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
    private final RefreshRepository refreshRepository;
    private final AccessTokenService accessTokenService;
    private final UserRepository userRepository;
    private final CookieUtil cookieUtil;

    public LogoutController(RefreshRepository refreshRepository,AccessTokenService accessTokenService,UserRepository userRepository,CookieUtil cookieUtil) {
        this.refreshRepository = refreshRepository;
        this.accessTokenService=accessTokenService;
        this.userRepository=userRepository;
        this.cookieUtil=cookieUtil;
    }

    @PostMapping("/logout")
    @Transactional
    public ResponseEntity<String> logout(@RequestBody Map<String, String> request,HttpServletResponse response) {

        String userEmail = request.get("userEmail");
        // body에서 액세스토큰 추출
        String accessToken = request.get("access");

        // Redis에서 액세스 토큰 삭제
        accessTokenService.deleteAccessToken(userEmail);

        // DB에서 리프레시 토큰 삭제
        Optional<UserEntity> userOptional = userRepository.findByUserEmail(userEmail);
        if (userOptional.isPresent()) {
            refreshRepository.deleteByUserEntity(userOptional.get());

            // 쿠키 만료 처리
            Cookie cookie = cookieUtil.createCookie("");
            cookie.setMaxAge(0);
            response.addCookie(cookie);

            return ResponseEntity.ok("로그아웃 성공");

        }
        return ResponseEntity.badRequest().body("사용자를 찾을 수 없습니다");
    }
}