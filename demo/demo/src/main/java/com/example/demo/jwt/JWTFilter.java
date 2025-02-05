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
        return path.equals("/api/login") ||
                path.equals("/api/join") ||
                path.equals("/api/logout") ||
                path.equals("/api/reissue") ||
                path.equals("/api/departments") ||
                path.startsWith("/api/teams");  // "/api/teams?departmentSeq=1" 같은 요청도 포함하기 위해 startsWith 사용

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //헤더에서 access키에 담긴 토큰을 꺼냄
        String accessToken = request.getHeader("access");

        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (accessToken != null) {
            //블랙리스트 토큰인지 확인
            if (authService.isTokenBlacklisted(accessToken)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token has been invalidated");
                return;
            }

            try {
                // 토큰 만료 확인
                jwtUtil.isExpired(accessToken);

                // 토큰 카테고리 확인
                String category = jwtUtil.getCategory(accessToken);
                if (!category.equals("access")) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Invalid access token");
                    return;
                }

                // 이메일 추출 및 토큰 검증
                String userEmail = jwtUtil.getUserEmail(accessToken);

                // Redis에 저장된 토큰과 비교
                if (!authService.validateAccessToken(userEmail, accessToken)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                // 인증 정보 설정
                setAuthentication(jwtUtil.getUserEmail(accessToken), jwtUtil.getRole(accessToken));
                filterChain.doFilter(request, response);
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("access token expired");
            }
        }

    }
    private void setAuthentication (String userEmail, String userRole){
        User user = new User();
        user.setUserEmail(userEmail);
        user.setUserRole(userRole);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                new CustomUserDetails(user),
                null,
                List.of(new SimpleGrantedAuthority(userRole))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
