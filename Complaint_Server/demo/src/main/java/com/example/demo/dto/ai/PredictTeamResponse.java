package com.example.demo.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PredictTeamResponse {
    private String 구분;
    private String 기관;
    private String 부서;
    private String 팀;
    private String 전화번호;
}