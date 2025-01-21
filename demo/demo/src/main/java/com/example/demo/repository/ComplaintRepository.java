package com.example.demo.repository;

import com.example.demo.entity.Complaint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long>{
    // 다중 조건 필터링
    @Query("SELECT c FROM Complaint c " +
            "WHERE (:memberSeq IS NULL OR c.member.memberSeq = :memberSeq) " +
            "AND (:departmentSeq IS NULL OR c.team.teamSeq = :teamSeq) " +
            "AND (:hasFile IS NULL OR (:hasFile = TRUE AND c.complaintFilePath IS NOT NULL) " +
            "OR (:hasFile = FALSE AND c.complaintFilePath IS NULL))")
    Page<Complaint> findByFilters(Long memberSeq, Long teamSeq, LocalDateTime startDate, LocalDateTime endDate, Boolean hasFile, Pageable pageable);
}
