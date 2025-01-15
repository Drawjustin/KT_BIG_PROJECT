package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import com.example.demo.entity.embeddable.FileDepartmentId;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "file_department")
public class FileDepartment extends baseEntity {

    @EmbeddedId
    private FileDepartmentId fileDepartmentId;
    @MapsId("fileSeq")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_seq")
    private File file;

    @MapsId("departmentSeq")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_seq")
    private Department department;

    @Builder
    public FileDepartment(FileDepartmentId fileDepartmentId, File file, Department department) {
        this.fileDepartmentId = fileDepartmentId;
        this.file = file;
        this.department = department;
    }
}


