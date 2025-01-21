package com.example.demo.controller;

import com.example.demo.dto.ComplaintCreateRequestDTO;
import com.example.demo.dto.ComplaintResponseDTO;
import com.example.demo.dto.ComplaintUpdateRequestDTO;
import com.example.demo.service.ComplaintService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;
import com.example.demo.entity.Complaint;
import java.time.LocalDateTime;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/complaint")
public class ComplaintController {
    private final ComplaintService complaintService;
    private static final Logger logger = LoggerFactory.getLogger(ComplaintController.class);

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }
    // 민원 등록
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<?> createComplaint(
            @RequestParam("memberSeq") Long memberSeq,
            @RequestParam("teamSeq") Long teamSeq,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            // 수동으로 DTO 생성
            ComplaintCreateRequestDTO request = ComplaintCreateRequestDTO.builder()
                    .memberSeq(memberSeq)
                    .teamSeq(teamSeq)
                    .title(title)
                    .content(content)
                    .file(file)
                    .build();

            log.info("Received complaint create request: {}", request);
            // 추가: 서비스 호출 전 로그
            log.info("About to call ComplaintService.createComplaint");

            // 서비스 호출
            ComplaintResponseDTO saved = complaintService.createComplaint(request);

            // 추가: 서비스 호출 후 로그
            log.info("ComplaintService.createComplaint call completed");

            if (saved != null && saved.getComplaintSeq() != null) {
                return ResponseEntity.ok().body(saved);
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Complaint creation failed: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }


    // 민원 수정
//    @PutMapping("/{id}/update")
//    public ResponseEntity<ComplaintResponseDTO> complaintUpdate(
//            @PathVariable Long id,
//            @ModelAttribute ComplaintUpdateRequestDTO updateRequestDTO) throws IOException {
//        logger.info("Updating complaint with ID: {}", id);
//
//        // 서비스 호출
//        ComplaintResponseDTO responseDTO = complaintService.updateComplaint(id, updateRequestDTO);
//
//        // 로그 출력
//        log.info("Response DTO in Controller: {}", responseDTO);
//
//        // 응답 반환
//        return ResponseEntity.ok(responseDTO);
//    }
//    // 민원 삭제
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> complaintDelete(@PathVariable Long id) {
//        logger.info("Deleting complaint with ID: {}", id);
//        complaintService.deleteComplaint(id);
//        return ResponseEntity.noContent().build();
//    }
//    // 민원 단건 조회
//    @GetMapping("/{id}")
//    public ResponseEntity<ComplaintResponseDTO> getComplaint(@PathVariable Long id) {
//        logger.info("Fetching complaint with ID: {}", id);
//        ComplaintResponseDTO responseDTO = complaintService.findComplaintById(id);
//        return ResponseEntity.ok(responseDTO);
//    }
//
//
//    // 민원 다건 조회
//    @GetMapping("/list")
//    public ResponseEntity<ComplaintListResponseDTO.PaginatedResponse> getComplaints(
//            @RequestParam(required = false) Long memberSeq,
//            @RequestParam(required = false) Long departmentSeq,
//            @RequestParam(required = false) String startDate,
//            @RequestParam(required = false) String endDate,
//            @RequestParam(required = false) Boolean hasFile,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "createdAt") String sortBy,
//            @RequestParam(defaultValue = "desc") String direction) {
//
//        LocalDateTime start = null, end = null;
//        try {
//            if (startDate != null) start = LocalDateTime.parse(startDate);
//            if (endDate != null) end = LocalDateTime.parse(endDate);
//        } catch (DateTimeParseException e) {
//            throw new IllegalArgumentException("Invalid date format", e);
//        }
//
//        // 페이징 설정
//        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
//        Pageable pageable = PageRequest.of(page, size, sort);
//
//        // 서비스 호출
//        ComplaintListResponseDTO.PaginatedResponse response = complaintService.findComplaintsByConditions(
//                memberSeq, departmentSeq, start, end, hasFile, pageable);
//
//        logger.info("Fetching complaints with pagination: page={}, size={}", page, size);
//        return ResponseEntity.ok(response);
//    }
}