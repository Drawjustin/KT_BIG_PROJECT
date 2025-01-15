package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "file")
public class File extends baseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileSeq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adminSeq")
    private Admin admin;
    private String fileTitle;
    private String fileContent;
    private String filePath;
    private String fileType;


}
