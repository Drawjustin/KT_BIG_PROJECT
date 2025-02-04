package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user") // 데이터베이스의 테이블 이름과 매핑
public class User extends baseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키에 자동 증가 설정
    @Column(name = "user_seq")
    private Long userSeq; // 기본 키로 설정된 user_seq

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_seq", nullable = false)
    private Team team;

    @Column(name = "user_email", unique = true, nullable = false, length = 255)
    private String userEmail; // 사용자의 이메일

    @Column(name = "user_id", length = 30, nullable = false)
    private String userId; // 사용자의 ID (최대 30자)

    @Column(name = "user_password", nullable = false, length = 255)
    private String userPassword; // 사용자의 비밀번호

    @Column(name = "user_name", nullable = false, length = 30)
    private String userName; // 사용자의 이름

    @Column(name = "user_number", length = 13)
    private String userNumber; // 사용자의 번호 (전화번호 등)

    @Column(name = "user_role", length = 50) // Role security에서 필요
    private String userRole;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Refresh refreshToken;

    // 모든 필드를 초기화하는 생성자 추가
    public User(String userEmail, String userId, String userPassword, String userName, String userNumber, String userRole) {
        this.userEmail = userEmail;
        this.userId = userId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userNumber = userNumber;
        this.userRole = (userRole != null) ? userRole : "USER"; // 기본값 설정 확인
    }

    public void setUserSeq(Long userSeq) {
        this.userSeq = userSeq;
    }

    public void setTeam(Team team) {this.team = team;}

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public void setRefreshToken(Refresh refreshToken) {
        this.refreshToken = refreshToken;
    }

    // 필요한 수정 메서드만 제공
    public void updatePassword(String newPassword) {
        this.userPassword = newPassword;
    }

    public void updateRefreshToken(Refresh refreshToken) {
        this.refreshToken = refreshToken;
    }
}
