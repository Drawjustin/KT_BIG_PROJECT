package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "complaint_comment")
public class ComplaintComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "complaint_comment_seq")
    private Long complaintCommentSeq;

    // Complaint와의 관계 (Many-to-One)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complaint_seq", nullable = false)
    private Complaint complaint;

    @Column(name = "complaint_comment_content", columnDefinition = "TEXT", nullable = false)
    private String complaintCommentContent;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted",nullable = false)
    private Boolean isDeleted;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.isDeleted = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Builder
    public ComplaintComment(Complaint complaint, String complaintCommentContent) {
        this.complaint = complaint;
        this.complaintCommentContent = complaintCommentContent;
        this.createdAt = LocalDateTime.now();
        this.isDeleted = false;
    }
    public void updateComment(String commentContent) {
        this.complaintCommentContent = commentContent;
        this.updatedAt = LocalDateTime.now();
    }

}
