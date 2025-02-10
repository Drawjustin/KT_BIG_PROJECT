package com.example.demo.repository.complaint;

import com.example.demo.dto.complaintDTO.ComplaintCommentResponseDTO;
import com.example.demo.dto.complaintDTO.ComplaintSearchCondition;
import com.example.demo.dto.complaintDTO.ComplaintListResponseDTO;
import com.example.demo.dto.complaintDTO.ComplaintResponseDTO;
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

import java.time.LocalDateTime;
import java.util.List;

import static com.example.demo.entity.QComplaint.complaint;
import static com.example.demo.entity.QDepartment.department;
import static com.example.demo.entity.QMember.member;
import static com.example.demo.entity.QTeam.team;

@RequiredArgsConstructor
@Repository
public class ComplaintRepositoryImpl implements ComplaintRepositoryCustom {
    private final JPAQueryFactory queryFactory;

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
                        ,isWithinTenDays()
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
                        isWithinTenDays()
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
}
