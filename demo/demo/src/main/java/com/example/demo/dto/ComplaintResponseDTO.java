package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplaintResponseDTO {
    private Long complaintSeq;          // 민원 고유번호
    private String memberName;          // 등록자 이름
    private String departmentName;      // 부서 이름
    private String title;               // 민원 제목
    private String content;             // 민원 내용
    private String filePath;            // 민원 파일 경로
    private LocalDateTime updatedAt;    // 수정 시간
}
