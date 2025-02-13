package com.example.demo.controller;

import com.example.demo.dto.CustomUserDetails;
import com.example.demo.dto.userDTO.ComplaintDailyStatDTO;
import com.example.demo.dto.userDTO.ComplaintSummaryDTO;
import com.example.demo.dto.userDTO.TelecomDTO;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/complaint-comments/profiles")
@RequiredArgsConstructor
public class UserController {

    private final UserService memberService;

    // 1. 전체/악성 민원 통계
    @GetMapping("/complaints/summary")
    public ResponseEntity<ComplaintSummaryDTO> getComplaintSummary(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(memberService.getMonthlyComplaintSummary(userDetails));
    }

    // 2. 일자별 민원 통계
    @GetMapping("/complaints/daily")
    public ResponseEntity<List<ComplaintDailyStatDTO>> getDailyStats(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(memberService.getDailyComplaintStats(userDetails));
    }

    // 3. 통화 기록 목록
    @GetMapping("/calls/daily")
    public ResponseEntity<List<TelecomDTO>> getCallRecords(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(memberService.getTodayTelecoms(userDetails));
    }
}