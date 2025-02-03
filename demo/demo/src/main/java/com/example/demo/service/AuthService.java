package com.example.demo.service;

import com.example.demo.config.JwtConfig;
import com.example.demo.config.SecurityConfig;
import com.example.demo.dto.CustomUserDetails;
import com.example.demo.dto.request.JoinRequest;
import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.response.AuthResponse;
import com.example.demo.dto.response.TokenResponse;
import com.example.demo.entity.RefreshEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.EmailAlreadyExistsException;
import com.example.demo.exception.InvalidTokenException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.jwt.JWTUtil;
import com.example.demo.repository.RefreshRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@Slf4j
public class AuthService {
    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final RefreshRepository refreshRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;

    private static final String ACCESS_TOKEN_PREFIX = "access_token:";

    public AuthService(
            JWTUtil jwtUtil,
            RedisTemplate<String, String> redisTemplate,
            RefreshRepository refreshRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Lazy AuthenticationManager authenticationManager,
            JwtConfig jwtConfig) {
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
        this.refreshRepository = refreshRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtConfig=jwtConfig;
    }

    // 회원가입 처리
    public void join(JoinRequest request) {
        if (userRepository.existsByUserEmail(request.userEmail())) {
            throw new EmailAlreadyExistsException("이미 사용 중인 이메일입니다.");
        }

        // JoinRequest의 toEntity 메서드를 사용하도록 수정
        UserEntity user = new UserEntity(
                request.userEmail(),
                request.userId(),
                passwordEncoder.encode(request.userPassword()),
                request.userName(),
                request.userNumber(),
                "USER"
        );

        userRepository.save(user);
    }

    // 로그인 처리
    public AuthResponse login(LoginRequest request) {
        // 인증 처리
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.userEmail(), request.userPassword())
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity user = userDetails.getUserEntity();

        // Access Token 생성
        String accessToken = createAccessToken(userDetails);

        // Refresh Token 생성 및 저장
        String refreshToken = createRefreshToken(userDetails);
        saveRefreshToken(user, refreshToken);

        return new AuthResponse(user.getUserEmail(), accessToken, refreshToken);
    }

    // 로그아웃 처리
    public void logout(String userEmail) {
        // Access Token 삭제
        deleteAccessToken(userEmail);

        // Refresh Token 삭제
        UserEntity user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        refreshRepository.deleteByUserEntity(user);
    }

    // 토큰 재발급
    public TokenResponse reissueToken(String refreshToken) {
        if (!validateRefreshToken(refreshToken)) {
            throw new InvalidTokenException("유효하지 않은 refresh token입니다.");
        }

        String userEmail = jwtUtil.getUserEmail(refreshToken);
        UserEntity user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        CustomUserDetails userDetails = new CustomUserDetails(user);

        // 새로운 Access Token 발급
        String newAccessToken = createAccessToken(userDetails);

        // Refresh Token 재발급 (선택적)
        String newRefreshToken = createRefreshToken(userDetails);
        saveRefreshToken(user, newRefreshToken);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    // Access Token 관련 메서드
    private String createAccessToken(CustomUserDetails userDetails) {
        String token = jwtUtil.createJwt(userDetails, "access", jwtConfig.getAccessTokenExpiration());
        saveAccessToken(userDetails.getUsername(), token);
        return token;
    }

    //Access토큰 redis에 저장
    private void saveAccessToken(String userEmail, String token) {
        String key = ACCESS_TOKEN_PREFIX + userEmail;
        redisTemplate.opsForValue().set(key, token, jwtConfig.getAccessTokenExpiration(), TimeUnit.MILLISECONDS);
    }

    private void deleteAccessToken(String userEmail) {
        redisTemplate.delete(ACCESS_TOKEN_PREFIX + userEmail);
    }

    // Refresh토큰 mysql에 저장
    private String createRefreshToken(CustomUserDetails userDetails) {
        return jwtUtil.createJwt(userDetails, "refresh", jwtConfig.getRefreshTokenExpiration());
    }

    private void saveRefreshToken(UserEntity user, String token) {
        RefreshEntity refreshToken = refreshRepository.findByUserEntity(user);
        if (refreshToken != null) {
            refreshToken.updateToken(token,
                    new Date(System.currentTimeMillis() + jwtConfig.getRefreshTokenExpiration()));
        } else {
            refreshToken = new RefreshEntity(
                    user,
                    token,
                    new Date(System.currentTimeMillis() + jwtConfig.getRefreshTokenExpiration())
            );
        }
        refreshRepository.save(refreshToken);
    }

    public boolean validateAccessToken(String userEmail, String accessToken) {
        String key = ACCESS_TOKEN_PREFIX + userEmail;
        String storedToken = redisTemplate.opsForValue().get(key);
        return storedToken != null && storedToken.equals(accessToken);
    }

    private boolean validateRefreshToken(String token) {
        return token != null && !jwtUtil.isExpired(token) &&
                jwtUtil.getCategory(token).equals("refresh");
    }
}