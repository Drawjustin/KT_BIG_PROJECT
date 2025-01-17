package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@Entity
@NoArgsConstructor
@Table(name = "telecom")
public class Telecom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "telecom_seq")
    private Long telecomSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_seq", nullable = false)
    private Department department;

    @Column(name = "telecom_content", columnDefinition = "TEXT")
    private String telecomContent;

    @Column(name = "telecom_file_path", length = 256)
    private String telecomFilePath;

    @Column(name = "is_complain")
    private Boolean isComplain;

    @Column(name = "telecom_count")
    private Byte telecomCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
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
    public Telecom(Department department, String telecomContent, String telecomFilePath, Boolean isComplain, Byte telecomCount) {
        this.department = department;
        this.telecomContent = telecomContent;
        this.telecomFilePath = telecomFilePath;
        this.isComplain = isComplain;
        this.telecomCount = telecomCount;
        this.createdAt = LocalDateTime.now();
        this.isDeleted = false;
    }
}
