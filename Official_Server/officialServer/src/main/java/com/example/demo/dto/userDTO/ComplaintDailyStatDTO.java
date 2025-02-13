package com.example.demo.dto.userDTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComplaintDailyStatDTO {
    private LocalDateTime date;
    private Long totalComplaints;
    private Long maliciousComplaints;
    // 반드시 필요한 생성자
    public ComplaintDailyStatDTO(LocalDateTime date, Long totalComplaints, Long maliciousComplaints) {
        this.date = date;
        this.totalComplaints = totalComplaints;
        this.maliciousComplaints = maliciousComplaints;
    }
}