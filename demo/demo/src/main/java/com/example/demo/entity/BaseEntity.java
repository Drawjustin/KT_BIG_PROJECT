package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public class BaseEntity {

    @CreationTimestamp // 자동으로 생성 시간 설정
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 데이터 생성 시간

    @UpdateTimestamp // 자동으로 업데이트 시간 설정
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 데이터 수정 시간

    @Column(name = "is_deleted")
    private Boolean isDeleted=false; // 삭제 여부, 디폴트 false

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
