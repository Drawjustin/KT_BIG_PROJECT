package com.example.demo.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public LoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // 클라이언트 요청에서 이메일(email)과 비밀번호(password) 추출
        String email = request.getParameter("email");
        String password = obtainPassword(request);

        // 이메일과 비밀번호를 검증하기 위해 Token 생성
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);

        // AuthenticationManager로 전달하여 인증 처리
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        // 로그인 성공 시 JWT 발급 등 처리
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("로그인 성공");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        // 로그인 실패 시 처리
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("로그인 실패: " + failed.getMessage());
    }
}
