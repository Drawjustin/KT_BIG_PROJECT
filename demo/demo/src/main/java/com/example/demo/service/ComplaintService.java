package com.example.demo.service;

import com.example.demo.dto.ComplaintCreateRequestDTO;
import com.example.demo.dto.ComplaintCreateResponseDTO;
import com.example.demo.dto.ComplaintUpdateRequestDTO;
import com.example.demo.dto.ComplaintResponseDTO;
import com.example.demo.entity.Team;
import com.example.demo.entity.Complaint;
import com.example.demo.entity.Member;
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

import static com.example.demo.exception.ErrorCodeCustom.*;


@Service
@Transactional
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;


    // 민원 등록
    @Transactional
    public ComplaintCreateResponseDTO createComplaint(ComplaintCreateRequestDTO request) {
        Complaint complaint = buildComplaint(request);
        return saveAndCreateResponse(complaint);
    }
    // 민원 업데이트
    @Transactional
    public String updateComplaint(Long id, ComplaintUpdateRequestDTO request) {
        Complaint complaint = findComplaintOrThrow(id);
        updateComplaintDetails(complaint, request);
        return "수정이 잘 되었습니다";
    }
    // 민원 삭제
    @Transactional
    public void deleteComplaint(Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RestApiException(NO_COMPLAINT));
        complaint.markAsDeleted();
    }

    // 민원 조회 (단건)
    @Transactional
    public ComplaintResponseDTO findComplaintById(Long id) {
        return complaintRepository.getComplaintById(id);
    }

    // 민원 페이지 조회 (여러건)
    @Transactional
    public Page<ComplaintResponseDTO> findComplaintsByConditions(Long complaintSeq, Pageable pageable){
        ComplaintCondition complaintCondition = new ComplaintCondition(complaintSeq);
        PageRequest page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return complaintRepository.getComplaints(complaintCondition,page);
    }

    private Complaint buildComplaint(ComplaintCreateRequestDTO request) {
        return Complaint.builder()
                .member(findMemberOrThrow(request.getMemberSeq()))
                .team(findTeamOrThrow(request.getTeamSeq()))
                .complaintTitle(request.getTitle())
                .complaintContent(request.getContent())
                .complaintFilePath(saveFile(request.getFile()))
                .build();
    }

    private Member findMemberOrThrow(Long memberSeq) {
        return memberRepository.findById(memberSeq)
                .orElseThrow(() -> new RestApiException(NO_MEMBER));
    }

    private Team findTeamOrThrow(Long teamSeq) {
        return teamRepository.findById(teamSeq)
                .orElseThrow(() -> new RestApiException(NO_DEPARTMENT));
    }

    private ComplaintCreateResponseDTO saveAndCreateResponse(Complaint complaint) {
        Long complaintSeq = complaintRepository.save(complaint).getComplaintSeq();
        return ComplaintCreateResponseDTO.builder()
                .complaintSeq(complaintSeq)
                .build();
    }

    public String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        return ExceptionHandlerUtil.executeWithCustomException(
                () -> saveFile(file),
                new RestApiException(FAIL_SAVED_FILE)
        );
    }

    private Complaint findComplaintOrThrow(Long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new RestApiException(NO_COMPLAINT));
    }



    private void updateComplaintDetails(Complaint complaint, ComplaintUpdateRequestDTO request) {
        complaint.updateComplaint(
                request.getTitle(),
                request.getContent(),
                saveFile(request.getFile())
        );
    }


}
