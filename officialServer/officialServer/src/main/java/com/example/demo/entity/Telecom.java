package com.example.demo.entity;


import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
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

    @Builder
    public Telecom(Department department, String telecomContent, String telecomFilePath, Boolean isComplain, Byte telecomCount) {
        this.department = department;
        this.telecomContent = telecomContent;
        this.telecomFilePath = telecomFilePath;
        this.isComplain = isComplain;
        this.telecomCount = telecomCount;
    }
}

