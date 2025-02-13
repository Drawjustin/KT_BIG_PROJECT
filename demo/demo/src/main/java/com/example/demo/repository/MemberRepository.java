package com.example.demo.repository;

import com.example.demo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Boolean existsByMemberEmail(String memberEmail);

    //userEmail을 받아 DB에서 회원을 조회하는  메소드
    Optional<Member> findByMemberEmail(String memberEmail);
}
