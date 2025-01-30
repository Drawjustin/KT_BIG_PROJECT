package com.example.demo.controller;

import com.example.demo.dto.complaintDTO.ComplaintResponseDTO;
import com.example.demo.dto.fileDto.FileListResponseDTO;
import com.example.demo.dto.fileDto.FileResponseDTO;
import com.example.demo.dto.fileDto.FileSearchCondition;
import com.example.demo.service.FileService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;


    //  TODO : 파일 단건 조회
    @GetMapping("/{fileSeq}")
    public ResponseEntity<FileResponseDTO> getComplaint(@PathVariable Long fileSeq) {FileResponseDTO responseDTO = fileService.findFilesById(fileSeq);
        return ResponseEntity.ok(responseDTO);
    }
    //  TODO : 파일 페이지 조회
    @GetMapping("")
    public ResponseEntity<Page<FileListResponseDTO>> findFilesByConditions(@ModelAttribute FileSearchCondition fileSearchCondition, Pageable pageable){
        Page<FileListResponseDTO> result = fileService.findFilesByConditions(fileSearchCondition, pageable);
        return ResponseEntity.ok(result);
    }
}
