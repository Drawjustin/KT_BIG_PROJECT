package com.example.demo.controller;

import com.example.demo.dto.ComplaintCreateRequestDTO;
import com.example.demo.dto.ComplaintResponseDTO;
import com.example.demo.entity.Member;
import com.example.demo.entity.Team;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.ComplaintService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.FileInputStream;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ComplaintController.class)
//@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ComplaintControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ComplaintController complaintController() {
            return Mockito.mock(ComplaintController.class);
        }

        @Bean
        public ComplaintService complaintService() {
            return Mockito.mock(ComplaintService.class);
        }

//        @Bean
//        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//            http
//                    // 전역적으로 CSRF 비활성화
//                    .csrf(csrf -> csrf.disable())
//                    .authorizeHttpRequests(auth -> auth
//                            // 어떤 요청이든 인증 없이 허용
//                            .anyRequest().permitAll()
//                    );
//
//            return http.build();
//        }
    }
//    @Configuration
//    static class WebConfig implements WebMvcConfigurer {
//
//        @Override
//        public void addCorsMappings(CorsRegistry registry) {
//            registry.addMapping("/**")
//                    .allowedOrigins("*")
//                    .allowedMethods("GET", "POST", "PUT", "DELETE")
//                    .allowedHeaders("*");
//        }
//    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ComplaintService complaintService; // Mock된 Service가 주입됨


    @Test
    void createComplaint() throws Exception {
        //Given
        final String fileName = "OfficialSOS"; //파일명
        final String contentType = "jpg"; //파일타입
        final String filePath = "C://KT_BIG_PROJECT//demo//demo//uploads//OfficialSOS.jpg"; //파일경로
        FileInputStream fileInputStream = new FileInputStream(filePath);

        //Mock파일생성
        MockMultipartFile image1 = new MockMultipartFile(
                "images", //name
                fileName + "." + contentType, //originalFilename
                contentType,
                fileInputStream
        );

        ComplaintCreateRequestDTO testComplaintDto = ComplaintCreateRequestDTO.builder()
                .title("test")
                .memberSeq(1L)
                .teamSeq(1L)
                .content("test")
                .file(image1)
                .build();

        Mockito.when(complaintService.createComplaint(Mockito.any()))
                .thenReturn(new ComplaintResponseDTO());

        ComplaintResponseDTO complaintResponseDTO = new ComplaintResponseDTO();
        ResultActions resultActions = mockMvc.perform(
                multipart("/complaint/create")
                        .file(image1) // 파일 추가
                        .param("memberSeq", String.valueOf(testComplaintDto.getMemberSeq())) // memberSeq 파라미터
                        .param("teamSeq", String.valueOf(testComplaintDto.getTeamSeq())) // teamSeq 파라미터
                        .param("title", testComplaintDto.getTitle()) // title 파라미터
                        .param("content", testComplaintDto.getContent()) // content 파라미터
                        .contentType(MediaType.MULTIPART_FORM_DATA) // Content-Type 설정
        );

        resultActions.andExpect(status().isOk());
    }

    @Test
    void complaintUpdate() {
    }

    @Test
    void complaintDelete() {
    }

    @Test
    void getComplaint() {
    }

    @Test
    void findComplaintsByConditions() {
    }
}