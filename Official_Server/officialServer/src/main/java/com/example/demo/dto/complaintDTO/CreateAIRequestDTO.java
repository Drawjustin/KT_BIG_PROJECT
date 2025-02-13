package com.example.demo.dto.complaintDTO;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAIRequestDTO {
    private String text;                // 민원 내용
    private int repeatCount;            // 반복 횟수
    private String class_department;    // 담당 부서
    private String tel;                 // 연락처
}
