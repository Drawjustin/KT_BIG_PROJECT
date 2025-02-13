package com.example.demo.dto.fileDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FileResponseDTO {
    private Long fileSeq;
    private String adminId;
    private String fileTitle;
    private String fileContent;
    private String filePath;
    private String fileType;
    private LocalDateTime updatedAt;
}
