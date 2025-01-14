package com.example.demo.entity;


import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "telecom")
public class Telecom extends baseEntity {

    @Id
    @GeneratedValue
    private Long telecomSeq;

    private Long departmentSeq;
    @Lob
    private String telecomContent;
    private String telecomFilePath;
    private Boolean isComplain;
    private Byte telecomCount;
}

