package com.example.demo.service;

import com.example.demo.jwt.JWTUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Transactional
@Slf4j
public class AuthService {
    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;


    private static final String ACCESS_TOKEN_PREFIX = "access_token:";

    public AuthService(
            JWTUtil jwtUtil,
            RedisTemplate<String, String> redisTemplate
    ) {
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
    }

    // 토큰 블랙리스트 관리
    public void blacklistToken(String token) {
        long remainingTime = jwtUtil.getRemainingTime(token);
        redisTemplate.opsForValue().set(
                "blacklist:" + token,
                "true",
                remainingTime,
                TimeUnit.MILLISECONDS
        );
    }

    // 블랙리스트 체크 메소드
    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(
                redisTemplate.hasKey("blacklist:" + token)
        );
    }

    public boolean validateAccessToken(String userEmail, String accessToken) {
        String key = ACCESS_TOKEN_PREFIX + userEmail + "member";
        String storedToken = redisTemplate.opsForValue().get(key);
        return storedToken != null && storedToken.equals(accessToken);
    }

}