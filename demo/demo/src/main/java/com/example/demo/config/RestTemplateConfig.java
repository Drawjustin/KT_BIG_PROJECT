package com.example.demo.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Getter
public class RestTemplateConfig {
    @Value("${complaint.predictMalcs_by_module.url}")
    private String predictMalcsUrl;

    @Value("${complaint.predictTeam_by_module.url}")
    private String predictTeamUrl;

    @Value("${complaint.predictDepartment_by_module.url}")
    private String predictDepartmentUrl;

    @Value("${complaint.repeatCount_by_module.url}")
    private String repeatCountUrl;

    @Value("${complaint.textSummary_by_module.url}")
    private String getTextSummaryUrl;
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}