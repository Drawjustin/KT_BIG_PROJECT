package com.example.demo.dto.fileDto.document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ExternalApiResponse {
    private String request_id;
    private String user_query;
    private List<SearchResult> high_results;
    private List<SearchResult> medium_results;
    private String base_answer;
    private List<Object> qna_list;
}