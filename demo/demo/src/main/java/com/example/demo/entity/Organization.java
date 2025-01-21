package com.example.demo.entity;
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
@Builder // Lombok Builder 추가
@Table(name = "organization")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organization_seq")
    private Long organizationSeq; // 기관 고유번호

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 기관 생성 시간

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 기관 업데이트 시간

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false; // 기관 삭제 여부

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Department> departments = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.isDeleted = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}