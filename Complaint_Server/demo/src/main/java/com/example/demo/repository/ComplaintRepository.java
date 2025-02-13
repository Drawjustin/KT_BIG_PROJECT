package com.example.demo.repository;

import com.example.demo.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long>, ComplaintRepositoryCustom{
    @Query("SELECT c.complaintSummary FROM Complaint c " +
            "WHERE c.member.memberSeq = :memberSeq " +
            "AND c.createdAt >= :startDate " +
            "ORDER BY c.createdAt DESC")
    List<String> findRecentComplaintSummariesByMemberSeq(
            @Param("memberSeq") Long memberSeq,
            @Param("startDate") LocalDateTime startDate);
}
