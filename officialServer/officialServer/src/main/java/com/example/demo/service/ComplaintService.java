package com.example.demo.service;

import com.example.demo.config.AIConfig;
import com.example.demo.dto.CustomUserDetails;
import com.example.demo.dto.complaintDTO.*;
import com.example.demo.entity.Complaint;
import com.example.demo.entity.ComplaintComment;
import com.example.demo.entity.Member;
import com.example.demo.entity.Team;
import com.example.demo.exception.RestApiException;
import com.example.demo.repository.*;
import com.example.demo.repository.complaint.ComplaintCommentRepository;
import com.example.demo.dto.complaintDTO.ComplaintSearchCondition;
import com.example.demo.repository.complaint.ComplaintRepository;
import com.example.demo.utils.ExceptionHandlerUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.demo.exception.ErrorCodeCommon.FAIL_SAVED_FILE;
import static com.example.demo.exception.ErrorCodeCustom.*;


@Service
@Transactional
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final ComplaintCommentRepository complaintCommentRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final DepartmentRepository departmentRepository;
    private final AIConfig aiConfig;

    // 민원 답변 등록
    @Transactional
    public void createComplaintComment(ComplaintCommentCreateRequestDTO request) {
        ComplaintComment complaintComment = buildComplaintComment(request);
        saveAndCreateResponse(complaintComment);
    }
    // 민원 답변 업데이트
    @Transactional
    public String updateComplaintComment(Long id, ComplaintCommentUpdateRequestDTO request) {
        ComplaintComment complaintComment = findComplaintCommentOrThrow(id);
        updateComplaintCommentDetails(complaintComment, request);
        return "수정이 잘 되었습니다";
    }

    // 민원 답변 삭제
    @Transactional
    public void deleteComplaintComment(Long id) {
        ComplaintComment complaintComment = findComplaintCommentOrThrow(id);
        complaintComment.markAsDeleted();
    }

    // 민원 조회 (단건)
    @Transactional
    public ComplaintResponseDTO findComplaintById(Long id) {
        return complaintRepository.getComplaintById(id);
    }

    // 민원 페이지 조회 (여러건)
    @Transactional
    public Page<ComplaintListResponseDTO> findComplaintsByConditions(ComplaintSearchCondition condition, Pageable pageable){
        PageRequest page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return complaintRepository.getComplaints(condition,page);
    }
    private ComplaintComment buildComplaintComment(ComplaintCommentCreateRequestDTO request) {
        return ComplaintComment.builder()
                .complaintCommentContent(request.getContent())
                .complaint(findComplaintOrThrow(request.getComplaintSeq()))
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

    private void saveAndCreateResponse(ComplaintComment complaintComment) {
        complaintCommentRepository.saveCustom(complaintComment);
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

    private ComplaintComment findComplaintCommentOrThrow(Long id){
        return complaintCommentRepository.findById(id)
                .orElseThrow(() -> new RestApiException(NO_COMPLAINT_COMMENT));
    }


    private void updateComplaintDetails(Complaint complaint, ComplaintUpdateRequestDTO request) {
        complaint.updateComplaint(
                request.getTitle(),
                request.getContent(),
                saveFile(request.getFile())
        );
    }

    private void updateComplaintCommentDetails(ComplaintComment complaintComment, ComplaintCommentUpdateRequestDTO request) {
        complaintComment.updateComplaintComment(
                request.getContent()
        );
    }
    private ResponseEntity<Map> restWebRequest(String url, HttpMethod method, HttpEntity<?> entity) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                url,
                method,
                entity,
                Map.class
        );
    }
    private HttpEntity<?> createHttpEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }
    public ComplaintCreateResponseDTO createAIResponse(CustomUserDetails userDetails, ComplaintCreateRequestDTO request) {

        String departmentNumber = departmentRepository.findDepartmentNumberByDepartmentName(request.getClass_department())
                .orElseThrow(() -> new RuntimeException("해당 부서를 찾을 수 없습니다."));
        String complaintCombined = complaintRepository.findComplaintCombinedByComplaintSeq(request.getComplaintSeq())
                .orElseThrow(() -> new RuntimeException("민원을 찾을 수 없습니다."));


        LocalDateTime tenDaysAgo = LocalDateTime.now().minusDays(10);
        List<Map<String, String>> pastComplaints = complaintRepository
                .findComplaintSummariesByComplaintSeqWithinTenDays(request.getComplaintSeq(), tenDaysAgo);


        AIComplaintRequestDTO aiRequest = AIComplaintRequestDTO.builder()
                .text(request.getComplaintText())  // 현재 민원 내용
                .pastComplaints(
                        pastComplaints.stream()
                                .map(complaint -> new AIComplaintRequestDTO.PastComplaint(complaint.get("text")))
                                .collect(Collectors.toList())
                )
                .build();

        System.out.println("aiRequest = " + aiRequest);

        ResponseEntity<Integer> repeatResponse = new RestTemplate().exchange(
                aiConfig.getComplaintRepeatURL(),
                HttpMethod.POST,
                createHttpEntity(aiRequest),
                Integer.class
        );
        Integer repeatResponseBody = repeatResponse.getBody();

        try {

            CreateAIRequestDTO createAIRequestDTO = CreateAIRequestDTO.builder()
                    .tel(departmentNumber)
                    .text(complaintCombined)
                    .repeatCount(repeatResponseBody)
                    .class_department(request.getClass_department())
                    .build();

            ResponseEntity<Map> createResponse = restWebRequest(
                    aiConfig.getComplaintResponseURL(),
                    HttpMethod.POST,
                    createHttpEntity(createAIRequestDTO)
            );

            Map<String, Object> responseBody = createResponse.getBody();
            return ComplaintCreateResponseDTO.builder()
                    .answer((String) responseBody.get("answer"))
                    .retrievedDocs((ArrayList<String>) responseBody.get("retrieved_docs"))
                    .build();

        } catch (RestClientException e) {
            throw new RuntimeException("AI 서버 통신 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

}
