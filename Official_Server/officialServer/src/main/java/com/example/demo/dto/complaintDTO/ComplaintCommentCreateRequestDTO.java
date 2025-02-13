package com.example.demo.dto.complaintDTO;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class ComplaintCommentCreateRequestDTO {
    private Long complaintSeq;    // 부서 정보
    private String content;
}