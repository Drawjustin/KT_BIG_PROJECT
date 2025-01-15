package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
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

    @Builder
    public File(Admin admin, String fileTitle, String fileContent, String filePath, String fileType) {
        this.admin = admin;
        this.fileTitle = fileTitle;
        this.fileContent = fileContent;
        this.filePath = filePath;
        this.fileType = fileType;
    }
}
