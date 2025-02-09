package com.example.demo.repository;

import com.example.demo.entity.Department;
import com.example.demo.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query("SELECT d FROM Department d WHERE d.departmentName = :name AND d.district.districtSeq = :districtSeq")
    Optional<Department> findByDepartmentNameAndDistrictSeq(
            @Param("name") String departmentName,
            @Param("districtSeq") Long districtSeq
    );
}
