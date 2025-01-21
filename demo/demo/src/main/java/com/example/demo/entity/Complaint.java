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
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "complaint")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Primary Key, AUTO_INCREMENT
    @Column(name = "complaint_seq")
    private Long complaintSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seq", nullable = false)
    @JsonManagedReference
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_seq",nullable = false)
    @JsonManagedReference
    private Department department;

    @OneToMany(mappedBy = "complaint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComplaintComment> complaintComments = new ArrayList<>();

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
    @Builder.Default
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
    public Complaint(Member member, Department department, String complaintTitle, String complaintContent, String complaintFilePath) {
        this.member = member; // Member 관계 설정
        this.department = department;
        this.complaintTitle = complaintTitle;
        this.complaintContent = complaintContent;
        this.complaintFilePath = complaintFilePath;
        this.createdAt = LocalDateTime.now();
    }

    // 업데이트 메서드 (필드만 업데이트)
    public void updateComplaint(String complaintTitle, String complaintContent, String complaintFilePath) {
        this.complaintTitle = complaintTitle;
        this.complaintContent = complaintContent;
        this.complaintFilePath = complaintFilePath;
        this.updatedAt = LocalDateTime.now();
    }

    // 민원 삭제
    public void markAsDeleted(){
        this.isDeleted=true;
    }

    // Member 할당 메서드
    public void assignMember(Member member) {
        this.member = member;
    }

    // Member 해제 메서드
    public void unassignMember() {
        this.member = null;
    }
    public Long getComplaintSeq() {
        return complaintSeq;
    }
}