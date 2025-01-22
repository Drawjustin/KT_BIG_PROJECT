package com.example.demo.jwt;

import com.example.demo.dto.CustomUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

//JWT 확인 및 발급
@Component
public class JWTUtil {

    @Value("${jwt.access-token.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName());
    }

    // CustomUserDetails 기반으로 JWT 생성 메소드
    public String createJwt(CustomUserDetails customUserDetails,
                            String category,
                            Long expiredMs) {
        // 토큰 만료 시간을 동적으로 설정
        long expirationTime = category.equals("access") ? accessTokenExpiration : refreshTokenExpiration;

        return Jwts.builder()
                .claim("category", category)
                .claim("userEmail", customUserDetails.getUsername())
                .claim("role", customUserDetails.getAuthorities().iterator().next().getAuthority())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }

    public String createJwt(CustomUserDetails customUserDetails, String category) {
        return createJwt(customUserDetails, category, 0L);
    }

    //email 가져와서 나의 토큰 키인지 확인
    public String getUserEmail(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("userEmail", String.class);
    }

    //role 가져와서 나의 토큰 키인지 확인
    public String getRole(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);

    }

    //토큰 타입을 식별 access인지 refresh인지
    public String getCategory(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("category", String.class);
    }

    //만료시간 확인
    public Boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }
}
