package com.example.demo.dto.response;

public record TeamResponse(
        Long teamSeq,
        String teamName,
        Long departmentSeq
) {}
