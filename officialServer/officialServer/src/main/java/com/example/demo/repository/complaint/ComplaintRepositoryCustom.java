package com.example.demo.repository.complaint;

import com.example.demo.dto.complaintDTO.ComplaintSearchCondition;
import com.example.demo.dto.complaintDTO.ComplaintListResponseDTO;
import com.example.demo.dto.complaintDTO.ComplaintResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintRepositoryCustom {
    ComplaintResponseDTO getComplaintById(long id);
    Page<ComplaintListResponseDTO> getComplaints(ComplaintSearchCondition condition, Pageable pageable);
}
