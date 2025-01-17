package com.example.demo.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class FileDepartmentId implements Serializable {

    private Long fileSeq;
    private Long departmentSeq;

    // 기본 생성자 필요 (Serializable 규칙)
    public FileDepartmentId() {}

    public FileDepartmentId(Long fileSeq, Long departmentSeq) {
        this.fileSeq = fileSeq;
        this.departmentSeq = departmentSeq;
    }
}