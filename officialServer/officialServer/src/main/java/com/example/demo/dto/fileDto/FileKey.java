package com.example.demo.dto.fileDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

// 그룹화를 위한 키 클래스
@Data
@AllArgsConstructor
public class FileKey {
    private Long fileSeq;
    private String adminId;
    private String title;
    private String content;
    private String filePath;
    private String fileType;
    private LocalDateTime updatedAt;
}
