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
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final CookieUtil cookieUtil;

    public ReissueService(
            UserRepository userRepository,
            TokenService tokenService,
            CookieUtil cookieUtil) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.cookieUtil = cookieUtil;
    }

    @Transactional
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        try {
            String refreshToken = cookieUtil.extractRefreshToken(request);

            if (!tokenService.validateRefreshToken(refreshToken)) {
                log.debug("Invalid or expired refresh token");
                return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                        .body("유효하지 않은 refresh token");
            }

            String userEmail = tokenService.getUserEmailFromToken(refreshToken);
            UserEntity userEntity = userRepository.findByUserEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

            CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

            // Access Token 생성
            String newAccessToken = tokenService.createNewAccessToken(customUserDetails);

            // Refresh Token 갱신 및 업데이트
            tokenService.createOrUpdateRefreshToken(userEntity, customUserDetails);

            // 쿠키 갱신
            response.addCookie(cookieUtil.createCookie(refreshToken));

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