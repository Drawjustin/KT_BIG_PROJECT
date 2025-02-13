package com.example.demo.repository.complaint;

import com.example.demo.dto.complaintDTO.ComplaintSearchCondition;
import com.example.demo.dto.complaintDTO.ComplaintListResponseDTO;
import com.example.demo.dto.complaintDTO.ComplaintResponseDTO;
import com.example.demo.dto.userDTO.ComplaintDailyStatDTO;
import com.example.demo.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ComplaintRepositoryCustom {
    ComplaintResponseDTO getComplaintById(long id);
    Page<ComplaintListResponseDTO> getComplaints(ComplaintSearchCondition condition, Pageable pageable);
    List<ComplaintDailyStatDTO> findDailyStatsByDepartmentAndDateBetween(
            Department department, LocalDateTime startDate, LocalDateTime endDate);

}
