package com.example.demo.dto.complaintDTO;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComplaintCommentUpdateRequestDTO {
    private String content;
}
