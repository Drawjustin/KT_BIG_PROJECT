package com.example.demo.jwt;

import com.example.demo.dto.CustomUserDetails;
import com.example.demo.entity.RefreshEntity;
import com.example.demo.repository.RefreshRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;// 로그인 엔드포인트

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Date;


// login 필터
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,RefreshRepository refreshRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshRepository=refreshRepository;
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
        //String userEmail = customUserDetails.getUsername();
        String userEmail = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // 토큰 발급
        String access = jwtUtil.createJwt("access",userEmail, role,600000L);
        String refresh = jwtUtil.createJwt("refresh",userEmail, role,86400000L);

        // Refresh 토큰 저장


        //응답 설정
        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        // 로그인 실패 시 처리
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"로그인 실패\", \"message\": \"" + failed.getMessage() + "\"}");
    }

    //refresh 저장메소드
    private void addRefreshEntity(String userEmail, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUserEmail(userEmail);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true); //https시 이걸 사용
        //cookie.setPath("/"); //https시 이걸 사용
        cookie.setHttpOnly(true);

        return cookie;
    }
}

