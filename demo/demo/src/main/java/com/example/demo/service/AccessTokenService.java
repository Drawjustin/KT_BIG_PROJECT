package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class AccessTokenService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private final String  KEY_PREFIX = "access_token:"; // Access Token prefix

    public AccessTokenService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 액세스 토큰을 Redis에 저장 (만료 시간 설정)
    public void saveAccessToken(String userEmail, String accessToken, long expireTime) {
        String key = KEY_PREFIX + userEmail;
        redisTemplate.delete(key);  // 기존 토큰 삭제
        redisTemplate.opsForValue().set(key, accessToken, expireTime, TimeUnit.MILLISECONDS);
    }

    public String getAccessToken(String userEmail) {
        return redisTemplate.opsForValue().get(KEY_PREFIX + userEmail);
    }

    public void deleteAccessToken(String userEmail) {
        redisTemplate.delete(KEY_PREFIX + userEmail);
    }
}
