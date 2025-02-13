package com.example.demo.repository.file;

import com.example.demo.dto.fileDto.FileListResponseDTO;
import com.example.demo.dto.fileDto.FileResponseDTO;
import com.example.demo.dto.fileDto.FileSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepositoryCustom {
    FileResponseDTO getFilesById(Long id);
    Page<FileListResponseDTO> getFiles(FileSearchCondition fileCondition, Pageable pageable);
}
