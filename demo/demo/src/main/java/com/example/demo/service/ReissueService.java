package com.example.demo.service;

import com.example.demo.config.JwtConfig;
import com.example.demo.dto.CustomUserDetails;
import com.example.demo.entity.RefreshEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.jwt.JWTUtil;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.CookieUtil;
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
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final AccessTokenService accessTokenService;
    private final CookieUtil cookieUtil;
    private final JwtConfig jwtConfig;

    public ReissueService(
            JWTUtil jwtUtil,
            UserRepository userRepository,
            RefreshTokenService refreshTokenService,
            AccessTokenService accessTokenService,
            CookieUtil cookieUtil,
            JwtConfig jwtConfig) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
        this.accessTokenService = accessTokenService;
        this.cookieUtil=cookieUtil;
        this.jwtConfig=jwtConfig;
    }

    @Transactional
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieUtil.extractRefreshToken(request);

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
            existingToken.updateToken(newRefreshToken, new Date(System.currentTimeMillis() + jwtConfig.getRefreshTokenExpiration()));

            // 새로운 access 토큰 생성
            String newAccessToken = jwtUtil.createJwt(customUserDetails, "access");
            accessTokenService.saveAccessToken(userEmail, newAccessToken, jwtConfig.getAccessTokenExpiration());

            // 새로운 리프레시 토큰을 쿠키에 설정
            response.addCookie(cookieUtil.createCookie(newRefreshToken));

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

}