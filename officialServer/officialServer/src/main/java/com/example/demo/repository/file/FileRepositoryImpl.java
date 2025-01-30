package com.example.demo.repository.file;


import com.example.demo.dto.complaintDTO.ComplaintCommentResponseDTO;
import com.example.demo.dto.complaintDTO.ComplaintResponseDTO;
import com.example.demo.dto.fileDto.FileKey;
import com.example.demo.dto.fileDto.FileListResponseDTO;
import com.example.demo.dto.fileDto.FileResponseDTO;
import com.example.demo.dto.fileDto.FileSearchCondition;
import com.example.demo.entity.Complaint;
import com.example.demo.entity.File;
import com.example.demo.entity.QAdmin;
import com.example.demo.entity.QFile;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.demo.entity.QAdmin.*;
import static com.example.demo.entity.QComplaint.complaint;
import static com.example.demo.entity.QDepartment.department;
import static com.example.demo.entity.QFile.*;
import static com.example.demo.entity.QFileTeam.fileTeam;
import static com.example.demo.entity.QMember.member;
import static com.example.demo.entity.QTeam.team;

@RequiredArgsConstructor
@Repository
public class FileRepositoryImpl implements FileRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public FileResponseDTO getFilesById(Long id) {
        return queryFactory
                .select(Projections.constructor(FileResponseDTO.class,
                        file.fileSeq,
                        admin.adminId,
                        file.fileTitle,
                        file.fileContent,
                        file.filePath,
                        file.fileType,
                        file.updatedAt))
                .from(file)
                .innerJoin(file.admin, admin)  // admin 별칭 추가
                .where(file.fileSeq.eq(id))
                .fetchOne();
    }
    @Override
    public Page<FileListResponseDTO> getFiles(FileSearchCondition fileCondition, Pageable pageable) {
        List<Tuple> tuples = queryFactory
                .select(
                        file.fileSeq,
                        admin.adminId,
                        file.fileTitle,
                        file.fileContent,
                        file.filePath,
                        file.fileType,
                        file.updatedAt,
                        team.department.departmentName
                )
                .from(file)
                .leftJoin(file.admin, admin)
                .leftJoin(file.fileTeams, fileTeam)
                .leftJoin(fileTeam.team, team)
                .leftJoin(team.department)
                .where(
                        departmentContains(fileCondition.getDepartmentName()),
                        titleContains(fileCondition.getTitle())
                )
                .orderBy(file.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 결과를 그룹화하여 DTO로 변환
        List<FileListResponseDTO> content = tuples.stream()
                .collect(Collectors.groupingBy(tuple ->
                                new FileKey(
                                        tuple.get(file.fileSeq),
                                        tuple.get(admin.adminId),
                                        tuple.get(file.fileTitle),
                                        tuple.get(file.fileContent),
                                        tuple.get(file.filePath),
                                        tuple.get(file.fileType),
                                        tuple.get(file.updatedAt)
                                ),
                        Collectors.mapping(
                                tuple -> tuple.get(team.department.departmentName),
                                Collectors.toList()
                        )))
                .entrySet().stream()
                .map(entry -> new FileListResponseDTO(
                        entry.getKey().getFileSeq(),
                        entry.getKey().getAdminId(),
                        entry.getValue().stream().anyMatch(Objects::nonNull) ? entry.getValue() : new ArrayList<>(),
                        entry.getKey().getTitle(),
                        entry.getKey().getContent(),
                        entry.getKey().getFilePath(),
                        entry.getKey().getFileType(),
                        entry.getKey().getUpdatedAt()
                ))
                .collect(Collectors.toList());

        // 전체 카운트 쿼리
        JPAQuery<Long> countQuery = queryFactory
                .select(file.countDistinct())
                .from(file)
                .leftJoin(file.fileTeams, fileTeam)
                .leftJoin(fileTeam.team, team)
                .leftJoin(team.department)
                .where(
                        departmentContains(fileCondition.getDepartmentName()),
                        titleContains(fileCondition.getTitle())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }


//    @Override
//    public Page<FileListResponseDTO> getFiles(FileSearchCondition fileCondition, Pageable pageable) {
//        List<FileListResponseDTO> content = queryFactory
//                .select(Projections.constructor(FileListResponseDTO.class,
//                        file.fileSeq,
//                        admin.adminId,
//                        file.fileTitle,
//                        file.fileContent,
//                        file.filePath,
//                        file.fileType,
//                        file.updatedAt))
//                .from(file)
//                .innerJoin(file.admin, admin)
//                .where(
//                        departmentContains(fileCondition.getDepartmentName()),
//                        titleContains(fileCondition.getTitle())
//                )
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        JPAQuery<Long> countQuery = queryFactory
//                .select(file.count())
//                .from(file)
//                .innerJoin(file.admin, admin)
//                .where(
//                        departmentContains(fileCondition.getDepartmentName()),
//                        titleContains(fileCondition.getTitle())
//                );
//
//            return PageableExecutionUtils.getPage(content,pageable, countQuery::fetchCount);
//
//    }
    private BooleanExpression departmentContains(String departmentName) {
        return departmentName != null ? department.departmentName.contains(departmentName) : null;
    }
    private BooleanExpression titleContains(String title) {
        return title != null ? file.fileTitle.contains(title) : null;
    }
}
