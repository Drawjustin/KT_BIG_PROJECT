package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ComplaintCreateRequestDTO {
    private Long memberSeq;        // 등록자 정보
    private Long departmentSeq;    // 부서 정보
    private String title;
    private String content;
    private MultipartFile file; // 파일 데이터를 업로드 받을 때 사용

    // toString 메서드 추가
    @Override
    public String toString() {
        return "ComplaintCreateRequestDTO{" +
                "memberSeq=" + memberSeq +
                ", departmentSeq=" + departmentSeq +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", file=" + (file != null ? file.getOriginalFilename() : "null") +
                '}';
    }
}