package com.example.demo.repository;

import com.example.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByUserEmail(String userEmail);

    //userEmail을 받아 DB에서 회원을 조회하는 메소드
    UserEntity findByUserEmail(String userEmail);
}
