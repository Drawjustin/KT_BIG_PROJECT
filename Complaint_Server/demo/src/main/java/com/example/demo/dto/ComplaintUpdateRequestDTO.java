package com.example.demo.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Setter
@ToString
public class ComplaintUpdateRequestDTO {
    private String title;          // 수정할 제목
    private String content;        // 수정할 내용
    private MultipartFile file;     // 새로 업로드할 파일
}