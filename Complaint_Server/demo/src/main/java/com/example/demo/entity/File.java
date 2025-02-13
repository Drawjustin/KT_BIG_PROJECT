package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
//@SQLDelete(sql = "UPDATE file SET is_deleted = true WHERE id = ?")
//@SQLRestriction("is_deleted = false")
@Table(name = "file")
public class File extends baseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_seq")
    private Long fileSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_seq", nullable = false)
    private Admin admin;

    @Column(name = "file_title", length = 256, nullable = false)
    private String fileTitle;

    @Column(name = "file_content", columnDefinition = "TEXT")
    private String fileContent;

    @Column(name = "file_path", length = 256)
    private String filePath;

    @Column(name = "file_type", length = 10)
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