package com.example.demo.jwt;

import com.example.demo.dto.CustomUserDetails;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
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
    private final UserRepository userRepository;

    public JWTFilter(JWTUtil jwtUtil, AuthService authService, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.authService=authService;
        this.userRepository = userRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/complaints/public/") || // 공개 엔드포인트
                path.equals("/api/login") ||
                path.equals("/api/join") ||
                path.equals("/api/logout") ||
                path.equals("/api/reissue");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{//Authorization 헤더에 Bearer로 토큰 전송
        String accessToken = request.getHeader("Authorization");

        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("No token provided");
            return;  // 중요: 여기서 필터 체인 종료
        }

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
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void setAuthentication (String userEmail, String userRole){
//        User user = new User();
        User user = userRepository.findByUserEmailWithTeamAndDepartment(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Authentication auth = new UsernamePasswordAuthenticationToken(
                new CustomUserDetails(user),
                null,
                List.of(new SimpleGrantedAuthority(userRole))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
