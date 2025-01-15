package com.example.demo.entity.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.MapsId;
import lombok.Builder;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FileDepartmentId implements Serializable {

    @Column(name = "file_seq")
    private Long fileSeq;

    @Column(name = "department_seq")
    private Long departmentSeq;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileDepartmentId that = (FileDepartmentId) o;
        return Objects.equals(fileSeq, that.fileSeq) &&
                Objects.equals(departmentSeq, that.departmentSeq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileSeq, departmentSeq);
    }
    @Builder
    public FileDepartmentId(Long fileSeq, Long departmentSeq) {
        this.fileSeq = fileSeq;
        this.departmentSeq = departmentSeq;
    }
}
