package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration

public class RestTemplateConfig {
    @Value("${searchPublic.response.url}")
    public String searchPublicResponseUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}