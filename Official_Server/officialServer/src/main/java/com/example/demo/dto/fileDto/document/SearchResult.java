package com.example.demo.dto.fileDto.document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SearchResult {
    private double score;
    private String title;
    private List<FileInfo> file_info;
    private String document_issue_date;
    private String ministry;
    private String department;
    private String summary;
}