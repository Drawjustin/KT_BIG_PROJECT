package com.example.demo.service;

import com.example.demo.config.RestTemplateConfig;
import com.example.demo.dto.CustomUserDetails;
import com.example.demo.dto.fileDto.FileListResponseDTO;
import com.example.demo.dto.fileDto.FileResponseDTO;
import com.example.demo.dto.fileDto.FileSearchCondition;
import com.example.demo.dto.fileDto.document.ExternalApiResponse;
import com.example.demo.dto.fileDto.document.SearchPublicDTO;
import com.example.demo.dto.fileDto.document.SearchResult;
import com.example.demo.repository.file.FileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final RestTemplate restTemplate;
    private final RestTemplateConfig restTemplateConfig;
    @Transactional
    public Page<FileListResponseDTO> findFilesByConditions(FileSearchCondition fileSearchCondition, Pageable pageable) {
        PageRequest page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return fileRepository.getFiles(fileSearchCondition,page);
    }

    public FileResponseDTO findFilesById(Long fileSeq) {
        return fileRepository.getFilesById(fileSeq);
    }


    public ExternalApiResponse searchPublic(CustomUserDetails userDetails, SearchPublicDTO searchPublicDTO) {
            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // HTTP 엔티티 생성
            HttpEntity<SearchPublicDTO> requestEntity = new HttpEntity<>(searchPublicDTO, headers);

            try {
                ResponseEntity<ExternalApiResponse> response = restTemplate.exchange(
                        restTemplateConfig.searchPublicResponseUrl,
                        HttpMethod.POST,
                        requestEntity,
                        ExternalApiResponse.class
                );

                return response.getBody();
            } catch (RestClientException e) {
                // 에러 처리
                throw new RuntimeException("External API call failed: " + e.getMessage());
            }


    }
}
