package com.example.demo.repository;

import com.example.demo.dto.ComplaintResponseDTO;
import com.example.demo.dto.ComplaintSearchCondition;
import com.example.demo.entity.Complaint;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.demo.entity.QComplaint.*;
import static com.example.demo.entity.QDepartment.*;
import static com.example.demo.entity.QMember.*;
import static com.example.demo.entity.QTeam.team;

@RequiredArgsConstructor
@Repository
public class ComplaintRepositoryImpl implements ComplaintRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public ComplaintResponseDTO getComplaintById(long id) {
        return queryFactory.select(Projections.constructor(ComplaintResponseDTO.class,
                        complaint.complaintSeq,
                        member.memberName,
                        department.departmentName,
                        complaint.complaintTitle,
                        complaint.complaintContent,
                        complaint.complaintFilePath,
                        complaint.updatedAt))
                .from(complaint)
                .innerJoin(complaint.member, member) // complaint와 member 조인
                .innerJoin(complaint.team, team) // complaint와 team 조인
                .leftJoin(team.department, department) // team과 department 조인
                .where(complaint.complaintSeq.eq(id))
                .fetchOne();
    }

    @Override
    public Page<ComplaintResponseDTO> getComplaints(ComplaintSearchCondition condition, Pageable pageable) {
        List<ComplaintResponseDTO> content = queryFactory
                .select(Projections.constructor(ComplaintResponseDTO.class,
                        complaint.complaintSeq,
                        member.memberName,
                        department.departmentName,
                        complaint.complaintTitle,
                        complaint.complaintContent,
                        complaint.complaintFilePath,
                        complaint.updatedAt))
                .from(complaint)
                .innerJoin(complaint.member, member) // complaint와 member 조인
                .innerJoin(complaint.team, team) // complaint와 team 조인
                .leftJoin(team.department, department) // team과 department 조인
                .where(departmentContains(condition.getDepartmentName()),titleContains(condition.getTitle()))
                .orderBy(complaint.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Complaint> countQuery = queryFactory
                .select(complaint)
                .from(complaint)
                .innerJoin(complaint.member, member) // complaint와 member 조인
                .innerJoin(complaint.team, team) // complaint와 team 조인
                .leftJoin(team.department, department) // team과 department 조인
                .where(
                        departmentContains(condition.getDepartmentName())
                        ,titleContains(condition.getTitle())
                );

        return PageableExecutionUtils.getPage(content,pageable, countQuery::fetchCount);
    }
    private BooleanExpression departmentContains(String departmentName) {
        return departmentName != null ? department.departmentName.contains(departmentName) : null;
    }
    private BooleanExpression titleContains(String title) {
        return title != null ? complaint.complaintTitle.contains(title) : null;
    }
}
