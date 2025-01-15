package com.example.demo.entity;


import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "telecom")
public class Telecom extends baseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long telecomSeq;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentSeq")
    private Department department;
    @Lob
    private String telecomContent;
    private String telecomFilePath;
    private Boolean isComplain;
    private Byte telecomCount;

}

