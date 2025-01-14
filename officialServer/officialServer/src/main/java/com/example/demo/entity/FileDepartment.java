package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "file_department")
public class FileDepartment extends baseEntity {

    @Id
    @GeneratedValue
    private Long fileSeq;

    private Long departmentSeq;
}
