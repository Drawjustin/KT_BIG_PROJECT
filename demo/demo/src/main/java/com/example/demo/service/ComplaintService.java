package com.example.demo.service;

import com.example.demo.config.FileStorageProperties;
import com.example.demo.dto.ComplaintCreateRequestDTO;
import com.example.demo.dto.ComplaintUpdateRequestDTO;
import com.example.demo.dto.ComplaintResponseDTO;
import com.example.demo.entity.Team;
import com.example.demo.exception.DepartmentNotFoundException;
import com.example.demo.exception.MemberNotFoundException;
import com.example.demo.entity.Complaint;
import com.example.demo.entity.Department;
import com.example.demo.entity.Member;

import com.example.demo.repository.ComplaintRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ComplaintService {

    private static final Logger logger = LoggerFactory.getLogger(ComplaintService.class);

    private final ComplaintRepository complaintRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;


    // 민원 등록
    @Transactional
    public ComplaintResponseDTO createComplaint(ComplaintCreateRequestDTO request) {
        log.info("Received ComplaintCreateRequestDTO: {}", request);

        try {
            // Step 1: Member 조회
            Member member = memberRepository.findById(request.getMemberSeq())
                    .orElseThrow(() -> new RuntimeException("Member not found"));

            // Step 2: Department 조회
            Team team = teamRepository.findById(request.getTeamSeq())
                    .orElseThrow(() -> new RuntimeException("Department not found"));

            // Step 3: 파일 처리
            String filePath = null;
            if (request.getFile() != null && !request.getFile().isEmpty()) {
                try {
                    filePath = saveFile(request.getFile());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save file", e);
                }
            }

            // Step 4: Complaint 엔티티 생성
            Complaint complaint = Complaint.builder()
                    .member(member)
                    .team(team)
                    .complaintTitle(request.getTitle())
                    .complaintContent(request.getContent())
                    .complaintFilePath(filePath)
                    .build();

            // Step 5: 저장
            Complaint savedComplaint = complaintRepository.save(complaint);

            // Step 6: DTO 변환 및 반환
//            ComplaintResponseDTO responseDTO = ComplaintResponseDTO.from(savedComplaint);
            return new ComplaintResponseDTO();

        } catch (Exception e) {
            log.error("Exception occurred during complaint creation: {}", e.getMessage(), e);
            throw e; // 예외를 컨트롤러로 재전달
        }
    }

    // 파일 저장 메서드
    public String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            log.warn("File is null or empty, skipping file save.");
            return null;
        }
        return file.getOriginalFilename();

//        // 파일 저장 경로
//        String uploadDir = "uploads/complaints/";
//        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//        Path uploadPath = Paths.get(uploadDir);
//
//        // 디렉토리 생성
//        if (!Files.exists(uploadPath)) {
//            log.info("Creating upload directory: {}", uploadPath);
//            Files.createDirectories(uploadPath);
//        }
//
//        // 파일 저장
//        Path filePath = uploadPath.resolve(fileName);
//        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//        log.info("File saved at: {}", filePath);

        // 저장된 파일 경로 반환
//        return uploadDir + fileName;
    }
//
//    // 민원 수정
//    @Transactional
//    public ComplaintResponseDTO updateComplaint(Long id, ComplaintUpdateRequestDTO updateRequestDTO) throws IOException {
//        log.info("Finding complaint with ID: {}", id);
//        // 기존 Complaint 객체 조회
//        Complaint complaint = complaintRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("해당 ID의 민원을 찾을 수 없습니다: " + id));
//        log.info("Found complaint: {}", complaint);
//        // 파일 업데이트: 새로운 파일이 업로드되었으면 저장, 아니면 기존 파일 경로 유지
//        String newFilePath = updateRequestDTO.getFile() != null && !updateRequestDTO.getFile().isEmpty()
//                ? saveFile(updateRequestDTO.getFile()) // 새로운 파일 저장
//                : updateRequestDTO.getFile() == null
//                ? null // 파일 삭제 요청 시
//                : complaint.getComplaintFilePath(); // 기존 파일 유지
//
//        // Complaint 업데이트
//        complaint.updateComplaint(
//                updateRequestDTO.getTitle(),
//                updateRequestDTO.getContent(),
//                newFilePath
//        );
//
//        return ComplaintResponseDTO.from(complaintRepository.save(complaint));
//    }
//
//    // 민원 삭제
//    @Transactional
//    public void deleteComplaint(Long id) {
//        Complaint complaint = complaintRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("해당 ID의 민원을 찾을 수 없습니다."));
//        complaint.markAsDeleted();
//        complaintRepository.save(complaint);
//    }
//
//    // 민원 조회 (단건)
//    @Transactional
//    public ComplaintResponseDTO findComplaintById(Long id) {
//        Complaint complaint = complaintRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("해당 ID의 민원을 찾을 수 없습니다: " + id));
//        return ComplaintResponseDTO.from(complaint);
//    }
//
//    @Transactional
//    public ComplaintListResponseDTO.PaginatedResponse findComplaintsByConditions(
//            Long memberSeq,
//            Long departmentSeq,
//            LocalDateTime startDate,
//            LocalDateTime endDate,
//            Boolean hasFile,
//            Pageable pageable) {
//
//        // 조건에 맞는 Page<Complaint> 조회
//        Page<Complaint> page = complaintRepository.findByFilters(memberSeq, departmentSeq, startDate, endDate, hasFile, pageable);
//
//        // Page<Complaint> -> PaginatedResponse 변환
//        return ComplaintListResponseDTO.fromPageWithPagination(page);
//    }
}
