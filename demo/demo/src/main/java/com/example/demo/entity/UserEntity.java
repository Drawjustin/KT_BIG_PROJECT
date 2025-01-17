package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user") // 데이터베이스의 테이블 이름과 매핑
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키에 자동 증가 설정
    @Column(name = "user_seq", nullable = false)
    private Long userSeq; // 기본 키로 설정된 user_seq

    @Column(name = "user_email", unique = true, nullable = false, length = 255)
    private String userEmail; // 사용자의 이메일

    @Column(name = "user_id", length = 30)
    private String userId; // 사용자의 ID (최대 30자)

    @Column(name = "user_password", nullable = false, length = 255)
    private String userPassword; // 사용자의 비밀번호

    @Column(name = "user_name", nullable = false, length = 255)
    private String userName; // 사용자의 이름

    @Column(name = "user_number", length = 13)
    private String userNumber; // 사용자의 번호 (전화번호 등)

    @Column(name = "user_role", nullable = false) // Role security에서 필요
    private String userRole="USER"; //디폴트 값 설정


    // 모든 필드를 초기화하는 생성자 추가
    public UserEntity(String userEmail, String userId, String userPassword, String userName, String userNumber, String userRole) {
        this.userEmail = userEmail;
        this.userId = userId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userNumber = userNumber;
        this.userRole = userRole != null ? userRole : "USER"; // null이면 "USER"로 기본값 설정
    }
}
