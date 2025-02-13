package com.example.demo.repository.complaint;

import com.example.demo.dto.complaintDTO.ComplaintCommentResponseDTO;
import com.example.demo.dto.complaintDTO.ComplaintSearchCondition;
import com.example.demo.dto.complaintDTO.ComplaintListResponseDTO;
import com.example.demo.dto.complaintDTO.ComplaintResponseDTO;
import com.example.demo.dto.userDTO.ComplaintDailyStatDTO;
import com.example.demo.entity.Complaint;
import com.example.demo.entity.Department;
import com.example.demo.entity.QComplaint;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.demo.entity.QComplaint.*;
import static com.example.demo.entity.QComplaint.complaint;
import static com.example.demo.entity.QDepartment.department;
import static com.example.demo.entity.QMember.member;
import static com.example.demo.entity.QTeam.team;

@RequiredArgsConstructor
@Repository
public class ComplaintRepositoryImpl implements ComplaintRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ComplaintDailyStatDTO> findDailyStatsByDepartmentAndDateBetween(
            Department department, LocalDateTime startDate, LocalDateTime endDate) {

        LocalDateTime startOfDay = startDate.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = endDate.withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        List<Tuple> results = queryFactory
                .select(
                        Expressions.stringTemplate(
                                "DATE_FORMAT({0}, '%Y-%m-%d')",
                                complaint.updatedAt).as("date"),
                        complaint.count(),
                        complaint.isBad.when(true).then(1L).otherwise(0L).sum())
                .from(complaint)
                .join(complaint.team, team)
                .where(team.department.eq(department)
                        .and(complaint.updatedAt.between(startOfDay, endOfDay)))
                .groupBy(Expressions.stringTemplate(
                        "DATE_FORMAT({0}, '%Y-%m-%d')",
                        complaint.updatedAt))
                .orderBy(complaint.updatedAt.asc())
                .fetch();

        List<LocalDateTime> allDates = new ArrayList<>();
        LocalDateTime currentDate = startOfDay;
        while (!currentDate.isAfter(endOfDay)) {
            allDates.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }

        Map<LocalDateTime, ComplaintDailyStatDTO> statsMap = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Tuple tuple : results) {
            String dateStr = tuple.get(0, String.class);
            LocalDateTime date = LocalDate.parse(dateStr, formatter).atStartOfDay();
            Long totalComplaints = tuple.get(1, Long.class);
            Long maliciousComplaints = tuple.get(2, Long.class);
            statsMap.put(date, new ComplaintDailyStatDTO(date, totalComplaints, maliciousComplaints));
        }

        return allDates.stream()
                .map(date -> statsMap.getOrDefault(date, new ComplaintDailyStatDTO(date, 0L, 0L)))
                .collect(Collectors.toList());
    }

       public ComplaintResponseDTO getComplaintById(long id) {
        // 1. 기본 정보와 댓글 리스트를 함께 조회
        Complaint result = queryFactory
                .selectFrom(complaint)
                .leftJoin(complaint.complaintComment).fetchJoin()
                .innerJoin(complaint.member, member).fetchJoin()
                .innerJoin(complaint.team, team).fetchJoin()
                .leftJoin(team.department, department).fetchJoin()
                .where(complaint.complaintSeq.eq(id))
                .fetchOne();

        // 2. 댓글 엔티티를 DTO로 변환
        List<ComplaintCommentResponseDTO> commentDTOs = result.getComplaintComment().stream()
                .map(comment -> new ComplaintCommentResponseDTO(
                        comment.getComplaintCommentSeq(),
                        comment.getComplaintCommentContent(),
                        comment.getUpdatedAt()))
                .toList();

        // 3. ComplaintResponseDTO 생성 및 반환
        return ComplaintResponseDTO.builder()
                .complaintSeq(result.getComplaintSeq())
                .commentResponseDTOList(commentDTOs)
                .memberName(result.getMember().getMemberName())
                .departmentName(result.getTeam().getDepartment().getDepartmentName())
                .title(result.getComplaintTitle())
                .content(result.getComplaintContent())
                .filePath(result.getComplaintFilePath())
                .date(result.getUpdatedAt())
                .isBad(result.getIsBad())
                .isAnswered(result.getIsAnswered())
                .summary(result.getComplaintSummary())
                .complaintCombined(result.getComplaintCombined())
                .complaintCount(result.getComplaintCount())
                .build();
    }

    @Override
    public Page<ComplaintListResponseDTO> getComplaints(ComplaintSearchCondition condition, Pageable pageable) {
        List<ComplaintListResponseDTO> content = queryFactory
                .select(Projections.constructor(ComplaintListResponseDTO.class,
                        complaint.complaintSeq,
                        member.memberName,
                        department.departmentName,
                        complaint.complaintTitle,
                        complaint.complaintContent,
                        complaint.complaintFilePath,
                        complaint.updatedAt,
                        complaint.isAnswered,
                        complaint.isBad))
                .from(complaint)
                .innerJoin(complaint.member, member) // complaint와 member 조인
                .innerJoin(complaint.team, team) // complaint와 team 조인
                .leftJoin(team.department, department) // team과 department 조인
                .where(
                        departmentSeqEquals(condition.getDepartmentSeq()),
                        departmentContains(condition.getDepartmentName())
                        ,titleContains(condition.getTitle())
                        , isAnsweredStatus(condition.getIsAnswered())
                        ,isWithinTenDays(),
                        notDeleted()
                )
                .orderBy(complaint.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(complaint.count()) // complaint 개수만 조회
                .from(complaint)
                // department 검색 조건이 있을 때만 필요한 조인
                .leftJoin(complaint.team, team)
                .leftJoin(team.department, department)
                .where(
                        departmentSeqEquals(condition.getDepartmentSeq()),
                        departmentContains(condition.getDepartmentName()),
                        titleContains(condition.getTitle()),
                        isAnsweredStatus(condition.getIsAnswered()),
                        isWithinTenDays(),
                        notDeleted()
                );

        return PageableExecutionUtils.getPage(content,pageable, countQuery::fetchOne);
    }
    private BooleanExpression departmentContains(String departmentName) {
        return departmentName != null ? department.departmentName.contains(departmentName) : null;
    }
    private BooleanExpression titleContains(String title) {
        return title != null ? complaint.complaintTitle.contains(title) : null;
    }

    private BooleanExpression isAnsweredStatus(Boolean isAnswered){
        return isAnswered != null ? complaint.isAnswered.eq(isAnswered) : null;
    }

    private BooleanExpression isWithinTenDays() {
        return complaint.updatedAt.after(LocalDateTime.now().minusDays(10));
    }
    private BooleanExpression departmentSeqEquals(Long departmentSeq) {
        return departmentSeq != null ? department.departmentSeq.eq(departmentSeq) : null;
    }
    private BooleanExpression notDeleted() {
        return complaint.isDeleted.isFalse();
    }
}
