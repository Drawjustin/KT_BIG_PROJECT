package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

//jwt 토큰 만료시간
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private Long expirationTime = 86400000L; // 기본값 24시간

    // getter, setter
    public Long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Long expirationTime) {
        this.expirationTime = expirationTime;
    }
}