package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class AccessTokenService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 액세스 토큰을 Redis에 저장 (만료 시간 설정)
    public void saveAccessToken(String accessToken, long expirationMillis) {
        redisTemplate.opsForValue().set(
                accessToken,
                "active",
                expirationMillis,
                TimeUnit.MILLISECONDS
        );
    }

    public String getAccessToken() {
        return redisTemplate.opsForValue().get("accessToken");
    }

    // 특정 액세스 토큰의 존재 여부 확인
    public boolean isAccessTokenInRedis(String accessToken) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(accessToken));
    }

    public void deleteAccessToken(String accessToken) {
        redisTemplate.delete(accessToken);  // 만료된 토큰 삭제
    }
}
