package com.example.demo.service;

import com.example.demo.entity.RefreshEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.jwt.JWTUtil;
import com.example.demo.repository.RefreshRepository;
import com.example.demo.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;


//쿠키: 만약에 access토큰 만료시 다시 만들어줌
@Service
public class ReissueService {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final UserRepository userRepository;

    public ReissueService(JWTUtil jwtUtil,RefreshRepository refreshRepository, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository=refreshRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        //Refresh토큰 가져오기
        String refresh = Arrays.stream(request.getCookies())
                .filter(cookie -> "refresh".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        if (refresh == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //토큰 만료 체크
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        //토큰이 refresh인지 확인(발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefreshTokenContent(refresh);
        if (!isExist) {

            //response body
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String userEmail = jwtUtil.getUserEmail(refresh);
        String userRole = jwtUtil.getRole(refresh);

        //새로운 access,refresh 토큰 발급
        String newAccess = jwtUtil.createJwt("access", userEmail, userRole, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", userEmail, userRole, 86400000L);

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshRepository.deleteByRefreshTokenContent(refresh);
        addRefreshEntity(userEmail, newRefresh, 86400000L);

        //response
        response.setHeader("access", newAccess);
        response.addCookie(createCookie("refresh", newRefresh));

        return ResponseEntity.ok(newAccess);
    }

    private void addRefreshEntity(String userEmail, String refresh, Long expiredMs) {
        java.util.Date expirationDate = new java.util.Date(System.currentTimeMillis() + expiredMs);

        // 사용자 이메일로 UserEntity를 조회
        UserEntity userEntity = userRepository.findByUserEmail(userEmail);
        if (userEntity == null) {
            throw new RuntimeException("User not found with email: " + userEmail);
        }

        RefreshEntity refreshEntity = new RefreshEntity(
                userEntity,              // UserEntity
                refresh,                 // refreshTokenContent
                expirationDate          // refreshTokenExpiration
        );

        refreshRepository.save(refreshEntity);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

}