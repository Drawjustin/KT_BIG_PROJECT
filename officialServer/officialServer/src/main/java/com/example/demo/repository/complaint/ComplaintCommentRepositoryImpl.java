package com.example.demo.repository.complaint;

import com.example.demo.entity.ComplaintComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ComplaintCommentRepositoryImpl implements ComplaintCommentRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;
    @Transactional
    public void saveCustom(ComplaintComment complaintComment){
        em.persist(complaintComment);
    }

}
