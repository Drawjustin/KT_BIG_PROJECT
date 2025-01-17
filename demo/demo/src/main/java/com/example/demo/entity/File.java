package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "file")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_seq")
    private Long fileSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_seq", nullable = false)
    private Admin admin;

    @Column(name = "file_title", length = 256, nullable = false)
    private String fileTitle;

    @Column(name = "file_content", columnDefinition = "TEXT")
    private String fileContent;

    @Column(name = "file_path", length = 256)
    private String filePath;

    @Column(name = "file_type", length = 10)
    private String fileType;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileDepartment> fileDepartments = new ArrayList<>();

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
    public File(Admin admin, String fileTitle, String fileContent, String filePath, String fileType) {
        this.admin = admin;
        this.fileTitle = fileTitle;
        this.fileContent = fileContent;
        this.filePath = filePath;
        this.fileType = fileType;
        this.createdAt = LocalDateTime.now();
        this.isDeleted = false;
    }
    public void updateFile(String title, String content, String filePath, String fileType) {
        this.fileTitle = title;
        this.fileContent = content;
        this.filePath = filePath;
        this.fileType = fileType;
        this.updatedAt = LocalDateTime.now();
    }
}