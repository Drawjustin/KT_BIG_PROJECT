package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@Entity
@NoArgsConstructor
@Table(name = "telecom")
public class Telecom extends baseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "telecom_seq")
    private Long telecomSeq;

    @Lob
    @Column(name = "telecom_content", columnDefinition = "TEXT")
    private String telecomContent;

    @Column(name = "telecom_file_path", length = 256)
    private String telecomFilePath;

    @Column(name = "is_complain")
    private Boolean isComplain;

    @Column(name = "telecom_count")
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
