package com.example.demo.service;

import com.example.demo.dto.fileDto.FileListResponseDTO;
import com.example.demo.dto.fileDto.FileResponseDTO;
import com.example.demo.dto.fileDto.FileSearchCondition;
import com.example.demo.repository.file.FileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    @Transactional
    public Page<FileListResponseDTO> findFilesByConditions(FileSearchCondition fileSearchCondition, Pageable pageable) {
        PageRequest page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return fileRepository.getFiles(fileSearchCondition,page);
    }

    public FileResponseDTO findFilesById(Long fileSeq) {
        return fileRepository.getFilesById(fileSeq);
    }
}
