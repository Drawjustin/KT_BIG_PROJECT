package com.example.demo.service;

import com.example.demo.config.JwtConfig;
import com.example.demo.dto.CustomUserDetails;
import com.example.demo.dto.request.JoinRequest;
import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.response.AuthResponse;
import com.example.demo.dto.response.TokenResponse;
import com.example.demo.entity.*;
import com.example.demo.exception.EmailAlreadyExistsException;
import com.example.demo.exception.InvalidTokenException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.jwt.JWTUtil;
import com.example.demo.repository.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@Slf4j
public class AuthService {
    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final MemberRepository memberRepository;
    private final RefreshTokenMemberRepository refreshTokenMemberRepository;

    private static final String ACCESS_TOKEN_PREFIX = "access_token:";

    public AuthService(
            JWTUtil jwtUtil,
            RedisTemplate<String, String> redisTemplate,
            PasswordEncoder passwordEncoder,
            @Lazy AuthenticationManager authenticationManager,
            JwtConfig jwtConfig,
            MemberRepository memberRepository,
            RefreshTokenMemberRepository refreshTokenMemberRepository
    ) {
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.memberRepository = memberRepository;
        this.refreshTokenMemberRepository = refreshTokenMemberRepository;
    }

    // 회원가입 처리
    public void join(JoinRequest request) {

        if (memberRepository.existsByMemberEmail(request.memberEmail())) {
            throw new EmailAlreadyExistsException("이미 사용 중인 이메일입니다.");
        }

        // 사용자 생성 및 저장
        Member member = request.toEntity(passwordEncoder);
        memberRepository.save(member);
    }

    // 로그인 처리
    public AuthResponse login(LoginRequest request) {
        // 인증 처리
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.memberEmail(), request.memberPassword())
        );

        CustomUserDetails memberDetails = (CustomUserDetails) authentication.getPrincipal();
        Member member = memberDetails.getMemberEntity();

        // Access Token 생성
        String accessToken = createAccessToken(memberDetails);

        // Refresh Token 생성 및 저장
        String refreshToken = createRefreshToken(memberDetails);
        saveRefreshToken(member, refreshToken);

        return new AuthResponse(member.getMemberEmail(), accessToken, refreshToken);
    }

    // 로그아웃 처리
    public void logout(String memberEmail, String accessToken) {
        // Access Token 삭제
        deleteAccessToken(memberEmail);

        // 블랙리스트에 Access Token 추가
        blacklistToken(accessToken);

        // Refresh Token 삭제
        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        refreshTokenMemberRepository.deleteByMember(member);
    }

    // 토큰 재발급
    public TokenResponse reissueToken(String refreshToken) {
        if (!validateRefreshToken(refreshToken)) {
            throw new InvalidTokenException("유효하지 않은 refresh token입니다.");
        }

        String memberEmail = jwtUtil.getUserEmail(refreshToken);
        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        CustomUserDetails userDetails = new CustomUserDetails(member);

        // 새로운 Access Token 발급
        String newAccessToken = createAccessToken(userDetails);

        // Refresh Token 재발급 (선택적)
        String newRefreshToken = createRefreshToken(userDetails);
        saveRefreshToken(member, newRefreshToken);

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
        String key = ACCESS_TOKEN_PREFIX + userEmail + "member";
        redisTemplate.opsForValue().set(key, token, jwtConfig.getAccessTokenExpiration(), TimeUnit.MILLISECONDS);
    }

    private void deleteAccessToken(String userEmail) {
        redisTemplate.delete(ACCESS_TOKEN_PREFIX + userEmail);
    }

    // Refresh토큰 mysql에 저장
    private String createRefreshToken(CustomUserDetails userDetails) {
        return jwtUtil.createJwt(userDetails, "refresh", jwtConfig.getRefreshTokenExpiration());
    }

    private void saveRefreshToken(Member member, String token) {
        RefreshTokenMember refreshTokenMember = refreshTokenMemberRepository.findByMember(member);
        if (refreshTokenMember != null) {
            refreshTokenMember.updateToken(token,
                    new Date(System.currentTimeMillis() + jwtConfig.getRefreshTokenExpiration()));
        } else {
            refreshTokenMember = new RefreshTokenMember(
                    member,
                    token,
                    new Date(System.currentTimeMillis() + jwtConfig.getRefreshTokenExpiration())
            );
        }
        refreshTokenMemberRepository.save(refreshTokenMember);
    }

    public boolean validateAccessToken(String userEmail, String accessToken) {
        String key = ACCESS_TOKEN_PREFIX + userEmail + "member";
        String storedToken = redisTemplate.opsForValue().get(key);
        return storedToken != null && storedToken.equals(accessToken);
    }

    private boolean validateRefreshToken(String token) {
        return token != null && !jwtUtil.isExpired(token) &&
                jwtUtil.getCategory(token).equals("refresh");
    }

    // 토큰을 블랙리스트에 추가
    public void blacklistToken(String token) {
        // 토큰의 남은 유효 시간 동안 블랙리스트에 보관
        long remainingTime = jwtUtil.getRemainingTime(token);

        redisTemplate.opsForValue().set(
                "blacklist:" + token,
                "true",
                Duration.ofMillis(remainingTime)
        );
    }

    // 토큰이 블랙리스트에 있는지 확인
    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(
                redisTemplate.hasKey("blacklist:" + token)
        );
    }


}