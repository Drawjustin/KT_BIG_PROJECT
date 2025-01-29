package com.example.demo.service;

import com.example.demo.dto.CustomUserDetails;
import com.example.demo.entity.RefreshEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.jwt.JWTUtil;
import com.example.demo.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;


@Service
@Slf4j
public class ReissueService {
    @Value("${jwt.access-token.expiration}")
    private long accessTokenExpiration; //액세스토큰

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;//리프레시토큰

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final AccessTokenService accessTokenService;

    public ReissueService(
            JWTUtil jwtUtil,
            UserRepository userRepository,
            RefreshTokenService refreshTokenService,
            AccessTokenService accessTokenService) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
        this.accessTokenService = accessTokenService;
    }

    @Transactional
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshToken(request);
        log.debug("Extracted Refresh Token: {}", refreshToken);

        if (refreshToken == null) {
            return ResponseEntity.badRequest().body("refresh token 없음");
        }

        try {
            if (jwtUtil.isExpired(refreshToken)) {
                log.debug("Refresh Token 만료");
                return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                        .body("refresh token 만료");
            }

            String userEmail = jwtUtil.getUserEmail(refreshToken);
            UserEntity userEntity = userRepository.findByUserEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("유저 없음"));

            CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

            // 기존 리프레시 토큰 찾아서 업데이트
            String newRefreshToken = jwtUtil.createJwt(customUserDetails, "refresh");
            RefreshEntity existingToken = refreshTokenService.findValidRefreshToken(userEntity);
            existingToken.updateToken(newRefreshToken, new Date(System.currentTimeMillis() + refreshTokenExpiration));

            // 새로운 access 토큰 생성
            String newAccessToken = jwtUtil.createJwt(customUserDetails, "access");
            accessTokenService.saveAccessToken(userEmail, newAccessToken, accessTokenExpiration);

            // 둘 다 반환
            response.addCookie(createCookie("refresh", newRefreshToken));

            return ResponseEntity.ok()
                    .body(Map.of("accessToken", newAccessToken));

        } catch (ExpiredJwtException e) {
            log.error("Token validation failed", e);
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                    .body("refresh token expired");
        } catch (Exception e) {
            log.error("Token reissue failed", e);
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .body("Token reissue failed");
        }
    }

    private String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        return Arrays.stream(cookies)
                .filter(cookie -> "refresh".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}