package com.example.demo.jwt;

import com.example.demo.config.JwtProperties;
import com.example.demo.dto.CustomUserDetails;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

// login 필터
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private Long expirationTime;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, JwtProperties jwtProperties) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.expirationTime = jwtProperties.getExpirationTime();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // JSON 요청 본문에서 email과 password를 추출
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> credentials = objectMapper.readValue(request.getInputStream(),
                    new TypeReference<Map<String, String>>() {});  // 타입 명시

            String userEmail = credentials.get("userEmail");
            String userPassword = credentials.get("userPassword");

            // 이메일과 비밀번호를 검증하기 위해 Token 생성
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userEmail, userPassword);

            // AuthenticationManager로 전달하여 인증 처리
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException("요청 본문에서 JSON 데이터를 읽는 중 오류 발생", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        // 로그인 성공 시 JWT 발급 등 처리
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String userEmail = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        // 토큰 발급
        String token = jwtUtil.createJwt(userEmail, role, expirationTime);

        // Header에 담아서 응답, 접두사 Bearer 추가
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Authorization", "Bearer " + token);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"message\": \"로그인 성공\", \"token\": \"Bearer " + token + "\"}");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        // 로그인 실패 시 처리
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"로그인 실패\", \"message\": \"" + failed.getMessage() + "\"}");
    }
}
