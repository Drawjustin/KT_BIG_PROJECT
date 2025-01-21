package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_seq")
    private Long departmentSeq; // 부서 고유번호

    @Column(name = "department_name", nullable = false, length = 50)
    private String departmentName; // 부서 이름

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_seq", nullable = false)
    private Organization organization; // Organization 관계 설정

    @OneToMany(mappedBy = "department")
    @JsonBackReference
    private List<Complaint> complaints; // 관련된 민원들

    @OneToMany(mappedBy = "department")
    private List<FileDepartment> fileDepartments; // 관련된 파일 부서

    @OneToMany(mappedBy = "department")
    private List<Block> blocks; // 관련된 차단

    @OneToMany(mappedBy = "department")
    private List<Telecom> telecoms; // 관련된 텔레콤

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성 시간

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 수정 시간

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false; // 삭제 여부

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getter
    public Long getDepartmentSeq() {
        return departmentSeq;
    }

    public String getDepartmentName() {
        return departmentName;
    }
    @Override
    public String toString() {
        return "Department{" +
                "departmentSeq=" + departmentSeq +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }


}