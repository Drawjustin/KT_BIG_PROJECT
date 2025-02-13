package com.example.demo.service;

import com.example.demo.dto.CustomUserDetails;
import com.example.demo.dto.userDTO.ComplaintDailyStatDTO;
import com.example.demo.dto.userDTO.ComplaintSummaryDTO;
import com.example.demo.dto.userDTO.TelecomDTO;
import com.example.demo.entity.Department;
import com.example.demo.entity.User;
import com.example.demo.repository.complaint.ComplaintRepository;
import com.example.demo.repository.user.TelecomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final ComplaintRepository complaintRepository;
    private final TelecomRepository telecomRepository;
    public ComplaintSummaryDTO getMonthlyComplaintSummary(CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        Department department = user.getTeam().getDepartment();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);  // 이번달 1일 00:00:00
//        LocalDateTime endOfMonth = now.withDayOfMonth(now.getMonth().length(now.toLocalDate().isLeapYear()))
//                .withHour(23).withMinute(59).withSecond(59);  // 이번달 말일 23:59:59


        ComplaintSummaryDTO summary = new ComplaintSummaryDTO();
        summary.setTotalComplaints(complaintRepository.countByTeam_DepartmentAndCreatedAtBetween(
                department, startOfMonth, now));
        summary.setMaliciousComplaints(complaintRepository.countByTeam_DepartmentAndIsBadTrueAndCreatedAtBetween(
                department, startOfMonth, now));
        return summary;
    }


    public List<ComplaintDailyStatDTO> getDailyComplaintStats(CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        Department department = user.getTeam().getDepartment();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);  // 이번달 1일 00:00:00

        return complaintRepository.findDailyStatsByDepartmentAndDateBetween(
                department, startOfMonth, now);  // 1일부터 현재 날짜까지
    }


    public List<TelecomDTO> getTodayTelecoms(CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0);  // 오늘 00:00:00
        LocalDateTime endOfDay = now.withHour(23).withMinute(59).withSecond(59); // 오늘 23:59:59
        Department department = user.getTeam().getDepartment();
        return telecomRepository.findByDepartmentAndCreatedAtBetween(department, startOfDay, endOfDay)
                .stream()
                .map(telecom -> TelecomDTO.builder()
                        .isComplain(telecom.getIsComplain())
                        .telecomFilePath(telecom.getTelecomFilePath())
                        .build())
                .collect(Collectors.toList());
    }
}