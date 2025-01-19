package com.example.demo.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

//JWT 확인 및 발급
@Component
public class JWTUtil {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public JWTUtil(@Value("${spring.jwt.privateKey}") String privateKeyStr,
                   @Value("${spring.jwt.publicKey}") String publicKeyStr) throws Exception {

        // Base64 디코딩 및 키 초기화
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyStr);
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
        this.publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
    }

    //email 가져와서 나의 토큰 키인지 확인
    public String getUserEmail(String token) {
        return Jwts.parser()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userEmail", String.class);
    }

    //role 가져와서 나의 토큰 키인지 확인
    public String getRole(String token) {
        return Jwts.parser()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    //만료시간 확인
    public Boolean isExpired(String token) {
        return Jwts.parser()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    //토큰생성 메소드
    public String createJwt(String userEmail, String role, Long expiredMs) {
        return Jwts.builder()
                .claim("userEmail", userEmail)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis())) //현재발행시간
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs)) //토큰만료=현재발행시간+토큰살아있는시간
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }
}
