package com.example.demo.repository;

import com.example.demo.entity.Refresh;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

//토큰 저장
public interface RefreshRepository extends JpaRepository<Refresh, Long> {

    void deleteByUser(User user);
    Refresh findByUser(User user); // 사용자 엔터티로 RefreshEntity 조회



}
