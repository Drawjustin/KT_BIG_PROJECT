package com.example.demo.service;

import com.example.demo.entity.RefreshEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.jwt.JWTUtil;
import com.example.demo.repository.RefreshRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class RefreshTokenService {
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
    public RefreshEntity saveRefreshToken(UserEntity userEntity, String newRefreshToken) {
        // 1. 기존 토큰이 있으면 무조건 삭제
        RefreshEntity existingToken = refreshRepository.findByUserEntity(userEntity);
        if (existingToken != null) {
            refreshRepository.delete(existingToken);
        }

        // 2. 새 토큰 생성 및 저장
        RefreshEntity newToken = new RefreshEntity(
                userEntity,
                newRefreshToken,
                new Date(System.currentTimeMillis() + 86400000L)  // 24시간 후 만료
        );

        // 3. 무조건 새 토큰으로 덮어쓰기
        return refreshRepository.save(newToken);
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
                new Date(System.currentTimeMillis() + 86400000L)
        );

        return refreshRepository.save(newToken);
    }

    //토큰 삭제 메서드
    @Transactional
    public void deleteRefreshToken(UserEntity userEntity) {
        RefreshEntity existingToken = refreshRepository.findByUserEntity(userEntity);
        if (existingToken != null) {
            refreshRepository.delete(existingToken);
        }
    }
}