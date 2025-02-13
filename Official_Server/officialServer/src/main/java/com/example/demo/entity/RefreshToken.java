package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

//토큰 저장 엔티티
@NoArgsConstructor // 기본 생성자
@Entity
@Getter
@Setter // RefreshEntity의 필드 수정 가능하도록 추가
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_seq", nullable = false)
    private Long refreshTokenSeq;

    @Column(name = "refresh_token_content", nullable = false, length = 512)
    private String refreshTokenContent;

    @Column(name = "refresh_token_expiration", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date refreshTokenExpiration;

    @OneToOne
    @JoinColumn(name = "user_seq", nullable = false, unique = true) // UserEntity의 PK를 FK로 사용
    private User user;


    public RefreshToken(User user, String refreshTokenContent, Date refreshTokenExpiration) {
        this.user = user;
        this.refreshTokenContent = refreshTokenContent;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }
    public void updateToken(String newToken, Date newExpiration) {
        this.refreshTokenContent = newToken;
        this.refreshTokenExpiration = newExpiration;
    }
}