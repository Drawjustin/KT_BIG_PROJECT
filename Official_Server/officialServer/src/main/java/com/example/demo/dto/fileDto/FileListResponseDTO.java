package com.example.demo.dto.fileDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class FileListResponseDTO {
    private Long fileSeq;
    private String adminName;
    private List<String> departmentName;
    private String title;
    private String content;
    private String filePath;
    private String fileType;
    private LocalDateTime updatedAt;
    // departmentNames가 null이나 비어있으면 빈 리스트 반환
}