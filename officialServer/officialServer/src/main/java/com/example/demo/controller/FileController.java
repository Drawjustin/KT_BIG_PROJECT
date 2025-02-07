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
    public ResponseEntity<FileResponseDTO> getfiles(@PathVariable Long fileSeq) {FileResponseDTO responseDTO = fileService.findFilesById(fileSeq);
        return ResponseEntity.ok(responseDTO);
    }
    //  TODO : 파일 페이지 조회
    @GetMapping("")
    public ResponseEntity<Page<FileListResponseDTO>> findFilesByConditions(@ModelAttribute FileSearchCondition fileSearchCondition, Pageable pageable){
        Page<FileListResponseDTO> result = fileService.findFilesByConditions(fileSearchCondition, pageable);
        return ResponseEntity.ok(result);
    }


    // TODO : 민원 답변 생성 API 3 끝

    // TODO : 공문서 검색 (Today Closed) 5
//    @PostMapping("/search")
//    public ResponseEntity<?> search(){
//
//    }
    // TODO : 국민신문고 민원생성 리뉴얼

    // TODO : 국민신문고 로그인 2 끝

    // TODO : DB 공문서 넣기 (DB 데이터 초기설정) 1

    // TODO : 국민신문고 수정 삭제 자기꺼만 RETURN 상태코드 변경으로 4

}
