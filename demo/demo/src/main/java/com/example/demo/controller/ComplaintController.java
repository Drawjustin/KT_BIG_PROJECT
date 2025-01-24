package com.example.demo.controller;

import com.example.demo.dto.ComplaintCreateRequestDTO;
import com.example.demo.dto.ComplaintResponseDTO;
import com.example.demo.dto.ComplaintUpdateRequestDTO;
import com.example.demo.service.ComplaintService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/complaints")
public class ComplaintController {
    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    // TODO : 민원 등록
    @PostMapping("")
    @ResponseBody
    public ResponseEntity<?> createComplaint (ComplaintCreateRequestDTO complaintCreateRequestDTO) {
        return ResponseEntity.ok().body(complaintService.createComplaint(complaintCreateRequestDTO));
    }

    // TODO : 민원 수정
    @PutMapping("/{complaintSeq}")
    public ResponseEntity<String> complaintUpdate(@PathVariable Long complaintSeq, @ModelAttribute ComplaintUpdateRequestDTO updateRequestDTO) {
        return ResponseEntity.ok(complaintService.updateComplaint(complaintSeq, updateRequestDTO));
    }

    // TODO : 민원 삭제
    @DeleteMapping("/{complaintSeq}")
    public ResponseEntity<String> complaintDelete(@PathVariable Long complaintSeq) {
        complaintService.deleteComplaint(complaintSeq);
        return ResponseEntity.noContent().build();
    }

    //  TODO : 민원 단건 조회
    @GetMapping("/{complaintSeq}")
    public ResponseEntity<ComplaintResponseDTO> getComplaint(@PathVariable Long complaintSeq) {
        return ResponseEntity.ok(complaintService.findComplaintById(complaintSeq));
    }

    //  TODO : 민원 페이지 조회
    @GetMapping("")
    public ResponseEntity<Page<ComplaintResponseDTO>> findComplaintsByConditions(@RequestParam(required = false) Long departmentSeq, Pageable pageable) {
        return ResponseEntity.ok(complaintService.findComplaintsByConditions(departmentSeq, pageable));
    }

}