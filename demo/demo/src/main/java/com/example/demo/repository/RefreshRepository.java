package com.example.demo.repository;

import com.example.demo.entity.RefreshEntity;
import com.example.demo.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

//토큰 저장
public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {

    Boolean existsByRefreshTokenContent(String refreshTokenContent); //refresh토큰이 존재하는지 확인메소드

    @Transactional
    void deleteByRefreshTokenContent(String refreshTokenContent); //만료된 refresh토큰 삭제메소드

    void deleteByUserEntity(UserEntity userEntity);

    RefreshEntity findByUserEntity(UserEntity userEntity); // 사용자 엔터티로 RefreshEntity 조회



}
