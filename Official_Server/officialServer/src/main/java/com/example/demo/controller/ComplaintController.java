package com.example.demo.controller;

import com.example.demo.dto.CustomUserDetails;
import com.example.demo.dto.complaintDTO.*;
import com.example.demo.service.ComplaintService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/complaint-comments")
public class ComplaintController {
    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    // TODO : 헬스 체크
    @GetMapping("/healthy")
    public ResponseEntity<?> healthy() {
        return ResponseEntity.ok().build();
    }

    // TODO : 민원 답변 등록
    @PostMapping("")
    public ResponseEntity<?> createComplaintComment (@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ComplaintCommentCreateRequestDTO complaintCommentCreateRequestDTO) {
        System.out.println("complaintCommentCreateRequestDTO = " + complaintCommentCreateRequestDTO);
        complaintService.createComplaintComment(complaintCommentCreateRequestDTO);
        return ResponseEntity.ok().build();
    }

    // TODO : 민원 답변 수정
    @PutMapping("/{complaintCommentSeq}")
    public ResponseEntity<String> complaintCommentUpdate(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long complaintCommentSeq, @RequestBody ComplaintCommentUpdateRequestDTO updateRequestDTO) {
        System.out.println("complaintCommentSeq = " + updateRequestDTO);
        return ResponseEntity.ok(complaintService.updateComplaintComment(complaintCommentSeq, updateRequestDTO));
    }

    // TODO : 민원 답변 삭제
    @DeleteMapping("/{complaintCommentSeq}")
    public ResponseEntity<String> complaintCommentDelete(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long complaintCommentSeq) {
        complaintService.deleteComplaintComment(complaintCommentSeq);
        return ResponseEntity.noContent().build();
    }

    //  TODO : 민원 단건 조회
    @GetMapping("/{complaintSeq}")
    public ResponseEntity<ComplaintResponseDTO> getComplaint(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long complaintSeq) {ComplaintResponseDTO responseDTO = complaintService.findComplaintById(complaintSeq);
        return ResponseEntity.ok(responseDTO);
    }

    //  TODO : 민원 페이지 조회
    @GetMapping("")
    public ResponseEntity<Page<ComplaintListResponseDTO>> findComplaintsByConditions(@AuthenticationPrincipal CustomUserDetails userDetails, @ModelAttribute ComplaintSearchCondition condition, Pageable pageable) {
        Long departmentSeq = userDetails.getUser().getTeam().getDepartment().getDepartmentSeq();
        condition.setDepartmentSeq(departmentSeq);
        Page<ComplaintListResponseDTO> complaints = complaintService.findComplaintsByConditions(condition, pageable);
        return ResponseEntity.ok(complaints);
    }

    // TODO : 민원 답변 생성
    @PostMapping("/create")
    public ResponseEntity<ComplaintCreateResponseDTO> createComplaintAnswer(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ComplaintCreateRequestDTO requestDTO) {
        System.out.println("requestDTO = " + requestDTO);
        ComplaintCreateResponseDTO response = complaintService.createAIResponse(userDetails, requestDTO);
            return ResponseEntity.ok(response);
    }

}