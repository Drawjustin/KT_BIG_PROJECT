package com.example.demo.controller;

import com.example.demo.entity.Complaint;
import com.example.demo.service.ComplaintService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/complaint")
public class ComplaintController {
    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService){
        this.complaintService = complaintService;
    }

    private static final Logger logger = LoggerFactory.getLogger(ComplaintController.class);
    // 민원 등록
    @PostMapping("/create")
    public ResponseEntity<Complaint> complaintCreate(
        @RequestParam Long userSeq,
        @RequestParam String title,
        @RequestParam String content,
        @RequestParam(required = false)MultipartFile file
    ) throws IOException{
        logger.info("UserSeq: {}", userSeq);
        logger.info("Title: {}", title);
        logger.info("Content: {}", content);
        logger.info("File: {}", (file != null ? file.getOriginalFilename() : "No file uploaded"));

        Complaint complaint = complaintService.complaintCreate(userSeq, title, content, file);
        return ResponseEntity.ok(complaint);
        }

    // 민원 수정
    @PutMapping("/{id}/update")
    public ResponseEntity<Complaint> ComplaintUpdate(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(required = false) MultipartFile file )
        throws IOException{
        Complaint complaintUpdate = complaintService.complaintUpdate(id, title, content, file);
        return ResponseEntity.ok(complaintUpdate);
    }

    // 민원 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Complaint> ComplaintDelete(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam (required = false) MultipartFile file)
        throws IOException{
        Complaint complaintDelete = complaintService.complaintDelete(id, title, content, file);
        return ResponseEntity.ok(complaintDelete);
    }


    // 민원 조회(단건)
    @GetMapping("/{id}")
    public ResponseEntity<Complaint> ComplaintList(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam (required = false) MultipartFile file
    ) throws IOException{
        Complaint complaintList = complaintService.complaintList(id, title, content, file);
        return ResponseEntity.ok(complaintList);
    }


    // 민원 조회(다건)



}
