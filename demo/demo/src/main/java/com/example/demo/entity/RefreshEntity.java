package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

//토큰 저장 엔티티
@Entity
@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
public class RefreshEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_seq", nullable = false)
    private Long refreshTokenSeq;

    @OneToOne
    @JoinColumn(name = "user_seq", nullable = false) // user_seq를 외래 키로 사용
    private UserEntity userEntity; // 연관된 사용자 엔터티

    @Column(name = "refresh_token_content", length = 500)
    private String refreshTokenContent;

    @Column(name = "refresh_token_expiration")
    @Temporal(TemporalType.TIMESTAMP)
    private Date refreshTokenExpiration;

    public RefreshEntity(String refreshTokenContent, Date refreshTokenExpiration) {
        this.refreshTokenContent = refreshTokenContent;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    // RefreshEntity.java에 추가
    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public void setRefreshTokenSeq(Long refreshTokenSeq) {
        this.refreshTokenSeq = refreshTokenSeq;
    }


    public void setRefreshTokenContent(String refreshTokenContent) {
        this.refreshTokenContent = refreshTokenContent;
    }

    public void setRefreshTokenExpiration(Date refreshTokenExpiration) {
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    // RefreshEntity 생성자 수정
    public RefreshEntity(UserEntity userEntity, String refreshTokenContent, Date refreshTokenExpiration) {
        this.userEntity = userEntity;
        this.refreshTokenContent = refreshTokenContent;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }
}
