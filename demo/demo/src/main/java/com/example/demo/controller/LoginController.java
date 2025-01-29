package com.example.demo.controller;

import com.example.demo.dto.CustomUserDetails;
import com.example.demo.entity.RefreshEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.jwt.JWTUtil;
import com.example.demo.repository.RefreshRepository;
import com.example.demo.service.AccessTokenService;
import com.example.demo.utils.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {
    @Value("${jwt.access-token.expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration}")
    private Long refreshTokenExpiration;

    private final AccessTokenService accessTokenService;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final RefreshRepository refreshRepository;
    private final RefreshTokenService refreshTokenService;
    private final CookieUtil cookieUtil;

    public LoginController(
            AccessTokenService accessTokenService,
            JWTUtil jwtUtil,
            AuthenticationManager authenticationManager,
            RefreshRepository refreshRepository,
            RefreshTokenService refreshTokenService,
            CookieUtil cookieUtil) {  // 생성자 주입
        this.accessTokenService = accessTokenService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.refreshRepository = refreshRepository;
        this.refreshTokenService=refreshTokenService;
        this.cookieUtil=cookieUtil;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials, HttpServletResponse response) {
        String userEmail = credentials.get("userEmail");
        String userPassword = credentials.get("userPassword");

        // 입력값 검증
        if (userEmail == null || userPassword == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "로그인 실패", "message", "이메일과 비밀번호를 입력해주세요"));
        }

        try {
            //인증 로직
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userEmail, userPassword);
            Authentication authentication = authenticationManager.authenticate(authToken);

            // CustomUserDetails로부터 정보를 가져와 JWT 토큰 생성
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            UserEntity user = customUserDetails.getUserEntity();

            // 토큰 생성 시 하드코딩 대신 위 변수 사용
            String access = jwtUtil.createJwt(customUserDetails, "access", accessTokenExpiration);
            String refresh = jwtUtil.createJwt(customUserDetails, "refresh", refreshTokenExpiration);
            // Redis에 access 토큰 저장
            accessTokenService.saveAccessToken(userEmail,access, accessTokenExpiration);

            // refres토큰 저장
            RefreshEntity refreshTokenEntity = refreshTokenService.saveOrReuseRefreshToken(user, refresh);

            // 쿠키 설정
            response.addCookie(cookieUtil.createCookie(refresh));

            //json에 acess토큰 발급
            return ResponseEntity.ok()
                    .body(Map.of(
                            "userEmail", user.getUserEmail(),
                            "accessToken", access
                    ));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                    .body(Map.of("error", "로그인 실패", "message", "이메일 또는 비밀번호가 잘못되었습니다"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "서버 오류", "message", "로그인 처리 중 오류가 발생했습니다"));
        }
    }
}

