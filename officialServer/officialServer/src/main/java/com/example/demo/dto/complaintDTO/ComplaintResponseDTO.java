package com.example.demo.dto.complaintDTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ComplaintResponseDTO {
    private Long complaintSeq;          // 민원 고유번호
    private List<ComplaintCommentResponseDTO> commentResponseDTOList;
    private String memberName;          // 등록자 이름
    private String departmentName;      // 부서 이름
    private String title;               // 민원 제목
    private String content;             // 민원 내용
    private String filePath;            // 민원 파일 경로
    private LocalDateTime date;    // 수정 시간

    private boolean isBad;
    private String summary;

    private String complaintCombined;
    private String complaintSummary;
    private Byte complaintCount;


}
