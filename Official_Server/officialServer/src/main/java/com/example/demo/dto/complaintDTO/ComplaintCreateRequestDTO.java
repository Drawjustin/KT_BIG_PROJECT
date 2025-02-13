package com.example.demo.dto.complaintDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
public class ComplaintCreateRequestDTO {
    private Long complaintSeq;
    private String complaintText;
    private String class_department;    // 담당 부서
}
