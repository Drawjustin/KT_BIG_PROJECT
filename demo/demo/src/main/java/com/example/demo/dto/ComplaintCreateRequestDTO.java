package com.example.demo.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class ComplaintCreateRequestDTO {
    private Long memberSeq;        // 등록자 정보
    private Long teamSeq;    // 부서 정보
    private String title;
    private String content;
    private MultipartFile file; // 파일 데이터를 업로드 받을 때 사용


}