package com.example.demo.service;

import com.example.demo.config.JwtConfig;
import com.example.demo.dto.CustomUserDetails;
import com.example.demo.entity.RefreshEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.jwt.JWTUtil;
import com.example.demo.repository.RefreshRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.Date;

@Service
@Slf4j
public class TokenService {
    private final JWTUtil jwtUtil;
    private final AccessTokenService accessTokenService;
    private final RefreshRepository refreshRepository;
    private final JwtConfig jwtConfig;

    public TokenService(
            JWTUtil jwtUtil,
            AccessTokenService accessTokenService,
            RefreshRepository  refreshRepository,
            JwtConfig jwtConfig) {
        this.jwtUtil = jwtUtil;
        this.accessTokenService = accessTokenService;
        this.refreshRepository = refreshRepository;
        this.jwtConfig = jwtConfig;
    }

    @Transactional
    public String createNewAccessToken(CustomUserDetails customUserDetails) {
        String accessToken = jwtUtil.createJwt(customUserDetails, "access");
        accessTokenService.saveAccessToken(
                customUserDetails.getUsername(),
                accessToken,
                jwtConfig.getAccessTokenExpiration()
        );
        return accessToken;
    }

    @Transactional
    public RefreshEntity findValidRefreshToken(UserEntity userEntity) {
        RefreshEntity existingToken = refreshRepository.findByUserEntity(userEntity);
        if (existingToken != null && !jwtUtil.isExpired(existingToken.getRefreshTokenContent())) {
            return existingToken;
        }
        return null;
    }


    @Transactional
    public RefreshEntity createOrUpdateRefreshToken(UserEntity userEntity, CustomUserDetails customUserDetails) {
        String refreshToken = jwtUtil.createJwt(customUserDetails, "refresh");
        RefreshEntity existingToken = refreshRepository.findByUserEntity(userEntity);

        if (existingToken != null && !jwtUtil.isExpired(existingToken.getRefreshTokenContent())) {
            return existingToken;
        }

        if (existingToken != null) {
            refreshRepository.delete(existingToken);
        }

        RefreshEntity newToken = new RefreshEntity(
                userEntity,
                refreshToken,
                new Date(System.currentTimeMillis() + jwtConfig.getRefreshTokenExpiration())
        );

        return refreshRepository.save(newToken);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return refreshToken != null && !jwtUtil.isExpired(refreshToken);
    }

    public String getUserEmailFromToken(String token) {
        return jwtUtil.getUserEmail(token);
    }

    public void deleteAccessToken(String userEmail) {
        accessTokenService.deleteAccessToken(userEmail);
    }
    
    @Transactional
    public void deleteRefreshToken(UserEntity userEntity) {
        refreshRepository.deleteByUserEntity(userEntity);
    }
}