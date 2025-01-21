package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void saveAccessToken(String token) {
        redisTemplate.opsForValue().set("accessToken", token);
    }

    public String getAccessToken() {
        return redisTemplate.opsForValue().get("accessToken");
    }
}
