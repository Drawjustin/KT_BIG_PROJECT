package com.example.demo.repository;

import com.example.demo.dto.ComplaintResponseDTO;
import com.example.demo.dto.ComplaintSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintRepositoryCustom {
    ComplaintResponseDTO getComplaintById(long id);
    Page<ComplaintResponseDTO> getComplaints(ComplaintSearchCondition condition, Pageable pageable);
}
