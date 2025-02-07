package com.example.demo.dto.fileDto.document;

import lombok.Data;

@Data
public class documentAnswerDTO {
    private BaseAnswerDTO baseAnswerDTO;
    private HighResultsSumDTO highResultsSumDTO;
    private MediumResultsSumDTO mediumResultsSumDTO;
    private FaQDTO faQDTO;
}
