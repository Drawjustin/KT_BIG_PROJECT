package com.example.demo.repository.user;

import com.example.demo.entity.Department;
import com.example.demo.entity.Telecom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TelecomRepository extends JpaRepository<Telecom, Long> {
    List<Telecom> findByDepartmentAndCreatedAtBetween(
            Department department,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    );
}