package com.example.demo.repository;

import com.example.demo.entity.Member;
import com.example.demo.entity.RefreshTokenMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenMemberRepository extends JpaRepository<RefreshTokenMember, Long> {

    void deleteByMember(Member member);

    RefreshTokenMember findByMember(Member member); // 사용자 엔터티로 RefreshEntity 조회
}
