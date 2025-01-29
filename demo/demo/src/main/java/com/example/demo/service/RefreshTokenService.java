package com.example.demo.service;

import com.example.demo.entity.RefreshEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.jwt.JWTUtil;
import com.example.demo.repository.RefreshRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class RefreshTokenService {
    @Value("${jwt.access-token.expiration}")
    private long accessTokenExpiration; //액세스토큰

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;//리프레시토큰

    private final RefreshRepository refreshRepository;
    private final JWTUtil jwtUtil;

    public RefreshTokenService(RefreshRepository refreshRepository, JWTUtil jwtUtil) {
        this.refreshRepository = refreshRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public RefreshEntity findValidRefreshToken(UserEntity userEntity) {
        RefreshEntity existingToken = refreshRepository.findByUserEntity(userEntity);

        // 토큰이 존재하고 유효한 경우
        if (existingToken != null &&
                !jwtUtil.isExpired(existingToken.getRefreshTokenContent())) {
            return existingToken;
        }

        return null;
    }

    @Transactional
    public RefreshEntity saveOrReuseRefreshToken(UserEntity userEntity, String newRefreshToken) {
        // 1. 기존 토큰 조회
        RefreshEntity existingToken = refreshRepository.findByUserEntity(userEntity);

        // 2. 기존 토큰이 존재하고 만료되지 않았다면 그대로 사용
        if (existingToken != null && !jwtUtil.isExpired(existingToken.getRefreshTokenContent())) {
            return existingToken;
        }

        // 3. 기존 토큰이 없거나 만료되었다면 새 토큰 저장
        if (existingToken != null) {
            refreshRepository.delete(existingToken);
        }

        RefreshEntity newToken = new RefreshEntity(
                userEntity,
                newRefreshToken,
                new Date(System.currentTimeMillis() + refreshTokenExpiration)
        );

        return refreshRepository.save(newToken);
    }

}