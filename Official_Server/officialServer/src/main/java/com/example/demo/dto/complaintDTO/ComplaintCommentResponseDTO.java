package com.example.demo.dto.complaintDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComplaintCommentResponseDTO {
    private Long complaintCommentSeq;
    private String content;
    private LocalDateTime updatedAt;
}
