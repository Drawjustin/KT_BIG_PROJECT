package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "file")
public class File extends baseEntity {

    @Id
    @GeneratedValue
    private Long fileSeq;

    private Long adminSeq;
    private String fileTitle;
    private String fileContent;
    private String filePath;
    private String fileType;

}
