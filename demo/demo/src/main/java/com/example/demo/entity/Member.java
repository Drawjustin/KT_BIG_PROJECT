package com.example.demo.entity;

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
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_seq")
    private Long memberSeq;

    @Column(name = "member_id", nullable = false, unique = true)
    private Integer memberId;

    @Column(name = "member_password", length = 512, nullable = false)
    private String memberPassword;

    @Column(name = "member_name", length = 30)
    private String memberName;

    @Column(name = "member_email", length = 30)
    private String memberEmail;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Block> blocks = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    @JsonManagedReference
    private List<Complaint> complaints = new ArrayList<>();

    @Override
    public String toString() {
        return "Member{" +
                "memberSeq=" + memberSeq +
                ", memberName='" + memberName + '\'' +
                '}';
    }

}