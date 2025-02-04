package com.example.demo.entity.baseEntity;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class baseEntity {


    @CreationTimestamp // 자동으로 생성 시간 설정
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 데이터 생성 시간

    @UpdateTimestamp // 자동으로 업데이트 시간 설정
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 데이터 수정 시간

    @Column(name = "is_deleted")
    private Boolean isDeleted=false; // 삭제 여부, 디폴트 false

//    @Column(updatable = false)
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
//
//    private Boolean isDeleted;
//    @PrePersist
//    public void prePersist() {
//        LocalDateTime now = LocalDateTime.now();
//        createdAt = now;
//        updatedAt = now;
//        isDeleted = false;
//    }
//    @PreUpdate
//    public void preUpdate(){
//        updatedAt = LocalDateTime.now();
//    }
//
//    public void markAsDeleted() {
//        this.isDeleted = true;
//    }
}
