package com.example.demo.dto;

import com.example.demo.entity.Complaint;
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
    private Long memberSeq;             // 등록자 고유번호
    private String memberName;          // 등록자 이름
    private Long departmentSeq;         // 부서 고유번호
    private String departmentName;      // 부서 이름
    private String title;               // 민원 제목
    private String content;             // 민원 내용
    private String filePath;            // 민원 파일 경로
    private LocalDateTime createdAt;    // 생성 시간
    private LocalDateTime updatedAt;    // 수정 시간
    private boolean isDeleted;          // 삭제 여부

}
