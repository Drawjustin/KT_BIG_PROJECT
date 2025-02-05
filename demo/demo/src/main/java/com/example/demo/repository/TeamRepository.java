package com.example.demo.repository;

import com.example.demo.entity.Department;
import com.example.demo.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findById(Long teamSeq);

    //부서 검색
    List<Team> findByDepartment(Department department);
}