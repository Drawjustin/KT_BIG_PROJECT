package com.example.demo.service;

import com.example.demo.dto.ComplaintCreateRequestDTO;
import com.example.demo.dto.ComplaintCreateResponseDTO;
import com.example.demo.dto.ComplaintUpdateRequestDTO;
import com.example.demo.dto.ComplaintResponseDTO;
import com.example.demo.entity.Team;
import com.example.demo.entity.Complaint;
import com.example.demo.entity.Member;

import com.example.demo.exception.ErrorCodeCustom;
import com.example.demo.exception.RestApiException;
import com.example.demo.repository.ComplaintCondition;
import com.example.demo.repository.ComplaintRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.TeamRepository;
import com.example.demo.utils.ExceptionHandlerUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;

import static com.example.demo.exception.ErrorCodeCustom.*;


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
    public ComplaintCreateResponseDTO createComplaint(ComplaintCreateRequestDTO request) {

        // Step 1: Member 조회
        Member member = memberRepository.findById(request.getMemberSeq())
                .orElseThrow(() -> new RestApiException(NO_MEMBER));

        // Step 2: Department 조회
        Team team = teamRepository.findById(request.getTeamSeq())
                .orElseThrow(() -> new RestApiException(NO_DEPARTMENT));

        // Step 3: 파일 처리
        String filePath = null;
        if (request.getFile() != null && !request.getFile().isEmpty()) {
            filePath = ExceptionHandlerUtil.executeWithCustomException(
                    () -> saveFile(request.getFile()),
                    new RestApiException(FAIL_SAVED_FILE)
            );
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
        Long complaintSeq = complaintRepository.save(complaint).getComplaintSeq();

        return ComplaintCreateResponseDTO.builder()
                .complaintSeq(complaintSeq)
                .build();

    }

    // 파일 저장 메서드
    public String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        return file.getOriginalFilename();
    }

    //
    // 민원 수정
    @Transactional
    public String updateComplaint(Long id, ComplaintUpdateRequestDTO updateRequestDTO) throws IOException {
        // 기존 Complaint 객체 조회
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 민원을 찾을 수 없습니다: " + id));

        // 파일 업데이트: 새로운 파일이 업로드되었으면 저장, 아니면 기존 파일 경로 유지
        String newFilePath = updateRequestDTO.getFile() != null && !updateRequestDTO.getFile().isEmpty()
                ? saveFile(updateRequestDTO.getFile()) // 새로운 파일 저장
                : updateRequestDTO.getFile() == null
                ? null // 파일 삭제 요청 시
                : complaint.getComplaintFilePath(); // 기존 파일 유지

        // Complaint 업데이트
        complaint.updateComplaint(
                updateRequestDTO.getTitle(),
                updateRequestDTO.getContent(),
                newFilePath
        );

        return "수정이 잘 되었습니다";
    }

    // 민원 삭제
    @Transactional
    public void deleteComplaint(Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 민원을 찾을 수 없습니다."));
        complaint.setIsDeleted(true);
    }

    // 민원 조회 (단건)
    @Transactional
    public ComplaintResponseDTO findComplaintById(Long id) {
        return complaintRepository.getComplaintById(id);
    }

    @Transactional
    public Page<ComplaintResponseDTO> findComplaintsByConditions(Long complaintSeq, Pageable pageable){
        ComplaintCondition complaintCondition = new ComplaintCondition();
        complaintCondition.setComplaintSeq(complaintSeq);
        PageRequest page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return complaintRepository.getComplaints(complaintCondition,page);

    }
}
