package com.example.demo.repository;

import com.example.demo.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query("SELECT d.departmentNumber FROM Department d WHERE d.departmentName = :departmentName")
    Optional<String> findDepartmentNumberByDepartmentName(@Param("departmentName") String departmentName);

    @Query("SELECT d.departmentNumber FROM Complaint c " +
            "JOIN c.team t " +
            "JOIN t.department d " +
            "WHERE c.complaintSeq = :complaintSeq")
    Optional<String> findDepartmentNumberByComplaintSeq(@Param("complaintSeq") Long complaintSeq);
}
