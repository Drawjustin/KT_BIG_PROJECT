package com.example.demo.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AIConfig {
    @Value("${complaint.response.url}")
    private String ComplaintResponseURL;
    @Value("${complaint.repeat.url}")
    private String ComplaintRepeatURL;
}
