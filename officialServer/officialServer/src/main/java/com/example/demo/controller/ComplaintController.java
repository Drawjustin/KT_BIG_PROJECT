package com.example.demo.controller;

import com.example.demo.dto.complaintDTO.ComplaintCommentCreateRequestDTO;
import com.example.demo.dto.complaintDTO.ComplaintCommentUpdateRequestDTO;
import com.example.demo.dto.complaintDTO.ComplaintListResponseDTO;
import com.example.demo.dto.complaintDTO.ComplaintResponseDTO;
import com.example.demo.dto.complaintDTO.ComplaintSearchCondition;
import com.example.demo.service.ComplaintService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/complaint-comments")
public class ComplaintController {
    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    // TODO : 민원 답변 등록
    @PostMapping("")
    public ResponseEntity<?> createComplaintComment (@RequestBody ComplaintCommentCreateRequestDTO complaintCommentCreateRequestDTO) {
        System.out.println("complaintCommentCreateRequestDTO = " + complaintCommentCreateRequestDTO);
        complaintService.createComplaintComment(complaintCommentCreateRequestDTO);
        return ResponseEntity.ok().build();
    }

    // TODO : 민원 답변 수정
    @PutMapping("/{complaintCommentSeq}")
    public ResponseEntity<String> complaintCommentUpdate(@PathVariable Long complaintCommentSeq, @RequestBody ComplaintCommentUpdateRequestDTO updateRequestDTO) {
        System.out.println("complaintCommentSeq = " + updateRequestDTO);
        return ResponseEntity.ok(complaintService.updateComplaintComment(complaintCommentSeq, updateRequestDTO));
    }

    // TODO : 민원 답변 삭제
    @DeleteMapping("/{complaintCommentSeq}")
    public ResponseEntity<String> complaintCommentDelete(@PathVariable Long complaintCommentSeq) {
        complaintService.deleteComplaintComment(complaintCommentSeq);
        return ResponseEntity.noContent().build();
    }

    //  TODO : 민원 단건 조회
    @GetMapping("/{complaintSeq}")
    public ResponseEntity<ComplaintResponseDTO> getComplaint(@PathVariable Long complaintSeq) {ComplaintResponseDTO responseDTO = complaintService.findComplaintById(complaintSeq);
        return ResponseEntity.ok(responseDTO);
    }

    //  TODO : 민원 페이지 조회
    @GetMapping("")
    public ResponseEntity<Page<ComplaintListResponseDTO>> findComplaintsByConditions(@ModelAttribute ComplaintSearchCondition condition, Pageable pageable) {
        System.out.println("condition = " + condition);
        Page<ComplaintListResponseDTO> complaints = complaintService.findComplaintsByConditions(condition, pageable);
        return ResponseEntity.ok(complaints);
    }

}