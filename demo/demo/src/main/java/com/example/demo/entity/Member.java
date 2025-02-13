package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "member") // 데이터베이스의 테이블 이름과 매핑
public class Member extends baseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키에 자동 증가 설정
    @Column(name = "member_seq")
    private Long memberSeq; // 기본 키로 설정된 member_seq

    @Column(name = "member_email", unique = true, nullable = false, length = 255)
    private String memberEmail; // 사용자의 이메일

    @Column(name = "member_id", length = 30, nullable = false)
    private String memberId; // 사용자의 ID (최대 30자)

    @Column(name = "member_password", nullable = false, length = 255)
    private String memberPassword; // 사용자의 비밀번호

    @Column(name = "member_name", nullable = false, length = 30)
    private String memberName; // 사용자의 이름

    @Column(name = "member_number", length = 13)
    private String memberNumber; // 사용자의 번호 (전화번호 등)

    @Column(name = "member_role", length = 50) // Role security에서 필요
    private String memberRole;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private RefreshTokenMember refreshTokenMember;

    // 모든 필드를 초기화하는 생성자 추가
    public Member(String memberEmail, String memberId, String memberPassword, String memberName, String memberNumber, String memberRole) {
        this.memberEmail = memberEmail;
        this.memberId = memberId;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
        this.memberNumber = memberNumber;
        this.memberRole = (memberRole != null) ? memberRole : "USER"; // 기본값 설정 확인
    }

    public void setMemberSeq(Long memberSeq) {
        this.memberSeq = memberSeq;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void setMemberPassword(String memberPassword) {
        this.memberPassword = memberPassword;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public void setMemberRole(String memberRole) {
        this.memberRole = memberRole;
    }

    public void setRefreshTokenMember(RefreshTokenMember refreshTokenMember) {
        this.refreshTokenMember = refreshTokenMember;
    }
}