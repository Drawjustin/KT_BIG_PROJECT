package com.example.demo.repository;

import com.example.demo.entity.QUser;
import com.example.demo.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.demo.entity.QUser.*;

@Repository
@Transactional
@RequiredArgsConstructor
public class TestRepository {

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    public User findUserById(String id){
        return queryFactory.selectFrom(user)
                .where(user.userId.eq(id))
                .fetchOne();
    }



}
