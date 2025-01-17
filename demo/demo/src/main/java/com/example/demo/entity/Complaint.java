package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "complaint")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Primary Key, AUTO_INCREMENT
    @Column(name = "complaint_seq")
    private Long complaintSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_seq", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seq", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_seq",nullable = false)
    private Department department;

    @Column(name = "complaint_title", length = 256)
    private String complaintTitle;

    @Column(name = "complaint_content", columnDefinition = "TEXT")
    private String complaintContent;

    @Column(name = "complaint_file_path", length = 256)
    private String complaintFilePath;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성 시간

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 수정 시간

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();  // 엔티티가 생성될 때 createdAt 설정
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();  // 엔티티가 업데이트될 때 updatedAt 설정
    }

    @Builder
    public Complaint(User user, Member member, Department department, String complaintTitle, String complaintContent, String complaintFilePath) {
        this.user = user;
        this.member = member; // Member 관계 설정
        this.department = department;
        this.complaintTitle = complaintTitle;
        this.complaintContent = complaintContent;
        this.complaintFilePath = complaintFilePath;
        this.createdAt = LocalDateTime.now();
        this.isDeleted = false;
    }

    // 업데이트 메서드 (필드만 업데이트)
    public void updateComplaint(String complaintTitle, String complaintContent, String complaintFilePath) {
        this.complaintTitle = complaintTitle;
        this.complaintContent = complaintContent;
        this.complaintFilePath = complaintFilePath;
        this.updatedAt = LocalDateTime.now();
    }
}