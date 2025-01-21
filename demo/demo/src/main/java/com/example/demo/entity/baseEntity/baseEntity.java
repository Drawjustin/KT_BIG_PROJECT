package com.example.demo.entity.baseEntity;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class baseEntity {
    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
        isDeleted = false;
    }
    @PreUpdate
    public void preUpdate(){
        updatedAt = LocalDateTime.now();
    }
}
