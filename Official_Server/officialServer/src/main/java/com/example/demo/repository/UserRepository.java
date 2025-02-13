package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //이메일 중복 체크
    Boolean existsByUserEmail(String userEmail);

    //userEmail을 받아 DB에서 회원을 조회하는  메소드
    Optional<User> findByUserEmail(String userEmail);

    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.team t " +
            "LEFT JOIN FETCH t.department " +
            "WHERE u.userEmail = :userEmail")
    Optional<User> findByUserEmailWithTeamAndDepartment(@Param("userEmail") String userEmail);
}
