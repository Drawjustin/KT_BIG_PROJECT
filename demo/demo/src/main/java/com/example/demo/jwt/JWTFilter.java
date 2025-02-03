package com.example.demo.jwt;

import com.example.demo.dto.CustomUserDetails;
import com.example.demo.entity.User;
import com.example.demo.service.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

//JWT 검증 필터
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final AuthService authService;

    public JWTFilter(JWTUtil jwtUtil, AuthService authService) {

        this.jwtUtil = jwtUtil;
        this.authService=authService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/complaints/public");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //헤더에서 access키에 담긴 토큰을 꺼냄
        String accessToken = request.getHeader("access");

        if (accessToken == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Access token이 필요합니다");
            return;
        }

        // 블랙리스트 토큰 체크 로직 추가
        if (authService.isTokenBlacklisted(accessToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("유효하지 않는 토큰입니다");
            return;
        }

        try {
            // 토큰 만료 확인 (현재 메서드는 예외를 던지므로 수정 필요)
            if (jwtUtil.isExpired(accessToken)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Access token 만료입니다");
                return;
            }

            String userEmail = jwtUtil.getUserEmail(accessToken);

            if (!authService.validateAccessToken(userEmail, accessToken)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("유효하지 않거나 만료된 토큰입니다");
                return;
            }

            setAuthentication(userEmail, jwtUtil.getRole(accessToken));
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("인증 실패: " + e.getMessage());
        }
    }
    private void setAuthentication(String userEmail, String userRole) {
        User userEntity = new User();
        userEntity.setUserEmail(userEmail);
        userEntity.setUserRole(userRole);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                new CustomUserDetails(userEntity),
                null,
                List.of(new SimpleGrantedAuthority(userRole))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

}
