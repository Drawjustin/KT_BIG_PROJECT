package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.ComplaintService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/minwon/complaints")
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

    // TODO : 민원 등록
    @PostMapping("")
    public ResponseEntity<?> createComplaint (@AuthenticationPrincipal CustomUserDetails userDetails, @ModelAttribute ComplaintCreateRequestDTO complaintCreateRequestDTO) {
        return ResponseEntity.ok().body(complaintService.createComplaint(userDetails, complaintCreateRequestDTO));
    }

    // TODO : 민원 수정
    @PutMapping("/{complaintSeq}")
    public ResponseEntity<String> complaintUpdate(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long complaintSeq, @ModelAttribute ComplaintUpdateRequestDTO updateRequestDTO) {
        System.out.println("updateRequestDTO = " + updateRequestDTO);
        return ResponseEntity.ok(complaintService.updateComplaint(userDetails,complaintSeq, updateRequestDTO));
    }

    // TODO : 민원 삭제
    @DeleteMapping("/{complaintSeq}")
    public ResponseEntity<String> complaintDelete(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long complaintSeq) {
        complaintService.deleteComplaint(userDetails,complaintSeq);
        return ResponseEntity.noContent().build();
    }

    //  TODO : 민원 단건 조회
    @GetMapping("/{complaintSeq}")
    public ResponseEntity<ComplaintResponseDTO> getComplaint(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long complaintSeq) {
        return ResponseEntity.ok(complaintService.findComplaintById(complaintSeq));
    }

    //  TODO : 민원 페이지 조회
    @GetMapping("")
    public ResponseEntity<Page<ComplaintResponseDTO>> findComplaintsByConditions(@AuthenticationPrincipal CustomUserDetails userDetails, @ModelAttribute ComplaintSearchCondition condition, Pageable pageable) {
        System.out.println("condition = " + condition);
        return ResponseEntity.ok(complaintService.findComplaintsByConditions(condition, pageable));
    }


}