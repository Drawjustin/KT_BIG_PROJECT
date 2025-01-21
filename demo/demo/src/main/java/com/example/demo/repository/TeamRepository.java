package com.example.demo.repository;

import com.example.demo.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    // 기본적으로 JpaRepository가 CRUD 메서드를 제공합니다.

    // 필요하면 커스텀 쿼리 추가 가능
    // 예: 부서 이름으로 검색
    Team findByTeamName(String teamName);
}