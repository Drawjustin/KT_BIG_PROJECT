package com.example.demo.controller;

import com.example.demo.service.ComplaintService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ComplaintControllerTestConfig {

    @Bean
    public ComplaintService complaintService() {
        return Mockito.mock(ComplaintService.class); // ComplaintService Mock 객체 생성
    }

    @Bean
    public ComplaintController complaintController(ComplaintService complaintService) {
        return new ComplaintController(complaintService); // Controller에 Mock Service 주입
    }
}