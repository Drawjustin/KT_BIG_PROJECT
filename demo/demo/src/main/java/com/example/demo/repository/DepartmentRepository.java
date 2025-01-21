package com.example.demo.repository;

import com.example.demo.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    // 기본적으로 JpaRepository가 CRUD 메서드를 제공합니다.

    // 필요하면 커스텀 쿼리 추가 가능
    // 예: 부서 이름으로 검색
    Department findByDepartmentName(String departmentName);
}