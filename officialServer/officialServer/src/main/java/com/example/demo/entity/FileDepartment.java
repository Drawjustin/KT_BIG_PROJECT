package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import com.example.demo.entity.embeddable.FileDepartmentId;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
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




}


