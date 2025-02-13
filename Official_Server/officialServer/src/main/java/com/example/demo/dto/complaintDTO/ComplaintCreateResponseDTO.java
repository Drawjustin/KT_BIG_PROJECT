package com.example.demo.dto.complaintDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
public class ComplaintCreateResponseDTO {
    private String answer;              // 민원 답변 내용
    private ArrayList<String> retrievedDocs;     // 참고된 문서들
}
