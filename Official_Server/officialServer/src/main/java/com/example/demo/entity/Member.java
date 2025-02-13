package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@SQLDelete(sql = "UPDATE member SET is_deleted = true WHERE id = ?")
//@SQLRestriction("is_deleted = false")
@Builder
@Table(name = "member")
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

    @Builder
    public Member(String memberId, String memberPassword, String memberName, String memberEmail) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
    }
}