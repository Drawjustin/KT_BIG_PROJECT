package com.example.demo.repository;

import com.example.demo.entity.RefreshEntity;
import com.example.demo.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

//토큰 저장
public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {

    void deleteByUserEntity(UserEntity userEntity);

    RefreshEntity findByUserEntity(UserEntity userEntity); // 사용자 엔터티로 RefreshEntity 조회



}
