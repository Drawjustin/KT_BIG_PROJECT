package com.example.demo.dto.userDTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TelecomDTO {
   private Boolean isComplain;     // 악성여부
   private String telecomFilePath; // 통화파일경로
}