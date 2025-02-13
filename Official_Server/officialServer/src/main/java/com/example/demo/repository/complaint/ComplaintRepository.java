package com.example.demo.repository.complaint;

import com.example.demo.entity.Complaint;
import com.example.demo.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long>, ComplaintRepositoryCustom{
    @Query("SELECT c.complaintCombined FROM Complaint c WHERE c.complaintSeq = :complaintSeq")
    Optional<String> findComplaintCombinedByComplaintSeq(@Param("complaintSeq") Long complaintSeq);



    @Query("SELECT new map(c.complaintSummary as text) " +
            "FROM Complaint c " +
            "WHERE c.member.memberSeq = (SELECT c2.member.memberSeq FROM Complaint c2 WHERE c2.complaintSeq = :complaintSeq) " +
            "AND c.updatedAt >= :tenDaysAgo " +
            "ORDER BY c.updatedAt DESC")
    List<Map<String, String>> findComplaintSummariesByComplaintSeqWithinTenDays(
            @Param("complaintSeq") Long complaintSeq,
            @Param("tenDaysAgo") LocalDateTime tenDaysAgo
    );


    // 팀에 속한 이번달 전체 민원 수
    int countByTeam_DepartmentAndCreatedAtBetween(Department department, LocalDateTime start, LocalDateTime end);

    // 팀에 속한 이번달 악성 민원 수
    int countByTeam_DepartmentAndIsBadTrueAndCreatedAtBetween(Department department, LocalDateTime start, LocalDateTime end);
}
