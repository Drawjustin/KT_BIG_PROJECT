package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@IdClass(FileDepartmentId.class)
@Table(name="file_department")
public class FileDepartment {

    @Id
    @Column(name = "file_seq", nullable = false, updatable = false)
    private Long fileSeq;

    @Id
    @Column(name = "department_seq", nullable = false, updatable = false)
    private Long departmentSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("fileSeq") // fileSeq와 연결
    @JoinColumn(name = "file_seq", nullable = false, updatable = false)
    private File file; // File 엔티티와의 관계

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("departmentSeq") // departmentSeq와 연결
    @JoinColumn(name = "department_seq", nullable = false, updatable = false)
    private Department department; // Department 엔티티와의 관계

    @Column(name = "created_at", nullable = false, updatable = false)
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

    public FileDepartment(File file, Department department) {
        this.file = file;
        this.department = department;
        this.fileSeq = file.getFileSeq(); // fileSeq 설정
        this.departmentSeq = department.getDepartmentSeq(); // departmentSeq 설정
        this.createdAt = LocalDateTime.now();
        this.isDeleted = false;
    }
}
