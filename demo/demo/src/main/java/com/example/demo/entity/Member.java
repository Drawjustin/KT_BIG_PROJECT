package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "member")
public class Member extends baseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_seq")
    private Long memberSeq;

    @Column(name = "member_id", length = 30, nullable = false, unique = true)
    private String memberId;

    @Column(name = "member_password", length = 512, nullable = false)
    private String memberPassword;

    @Column(name = "member_name", length = 30)
    private String memberName;

    @Column(name = "member_email", length = 30)
    private String memberEmail;

    @Builder
    public Member(String memberId, String memberPassword, String memberName, String memberEmail) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
    }
}