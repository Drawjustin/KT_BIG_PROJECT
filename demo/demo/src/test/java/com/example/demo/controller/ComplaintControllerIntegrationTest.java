package com.example.demo.controller;

import com.example.demo.dto.ComplaintCreateRequestDTO;
import com.example.demo.dto.ComplaintResponseDTO;
import com.example.demo.entity.Complaint;
import com.example.demo.entity.Department;
import com.example.demo.entity.Member;
import com.example.demo.entity.Organization;
import com.example.demo.repository.ComplaintRepository;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.OrganizationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;
import java.util.Optional;

import static com.example.demo.entity.QOrganization.organization;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // 데이터 정합성 보장 (테스트 후 롤백)
public class ComplaintControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private OrganizationRepository organizationRepository;


    @Autowired
    private ObjectMapper objectMapper; // JSON 직렬화/역직렬화

    private Member savedMember;
    private Department savedDepartment;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 초기화
        complaintRepository.deleteAll();
        departmentRepository.deleteAll();
        memberRepository.deleteAll();
        organizationRepository.deleteAll();

        Organization organization = organizationRepository.saveAndFlush(
                Organization.builder()
                        .build()
        );
        savedMember = memberRepository.saveAndFlush(
                Member.builder()
                        .memberId(1) // 멤버 ID를 1로 설정
                        .memberName("Test Member")
                        .memberPassword("password123")
                        .memberEmail("test@example.com")
                        .build()
        );
        savedDepartment = departmentRepository.saveAndFlush(
                Department.builder()
                        .departmentName("Test Department")
                        .organization(organization) // ManyToOne 관계 설정
                        .build()
        );
    }

    @Test
    void testCreateComplaint() throws Exception {
        // 요청 데이터 생성
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Test File Content".getBytes()
        );

        // 요청 수행
        mockMvc.perform(multipart("/complaint/create")
                        .file(file)
                        .param("memberSeq", String.valueOf(savedMember.getMemberSeq()))
                        .param("departmentSeq", String.valueOf(savedDepartment.getDepartmentSeq()))
                        .param("title", "Test Title")
                        .param("content", "Test Content")
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.content").value("Test Content"));

        // DB 검증
        Optional<Complaint> savedComplaint = complaintRepository.findAll().stream().findFirst();
        assertTrue(savedComplaint.isPresent());
        assertEquals("Test Title", savedComplaint.get().getComplaintTitle());
        assertEquals("Test Content", savedComplaint.get().getComplaintContent());
    }

    @Test
    void testUpdateComplaint() throws Exception {
        // 기존 민원 저장
        Complaint savedComplaint = complaintRepository.save(
                Complaint.builder()
                        .member(savedMember)
                        .department(savedDepartment)
                        .complaintTitle("Old Title")
                        .complaintContent("Old Content")
                        .build()
        );

        // 업데이트 요청 데이터
        MockMultipartFile file = new MockMultipartFile(
                "file", "updated.txt", MediaType.TEXT_PLAIN_VALUE, "Updated Content".getBytes()
        );

        // 요청 수행
        mockMvc.perform(multipart("/complaint/" + savedComplaint.getComplaintSeq() + "/update")
                        .file(file)
                        .param("title", "Updated Title")
                        .param("content", "Updated Content")
                        .with(csrf())
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.content").value("Updated Content"));

        // DB 검증
        Complaint updatedComplaint = complaintRepository.findById(savedComplaint.getComplaintSeq()).orElseThrow();
        assertEquals("Updated Title", updatedComplaint.getComplaintTitle());
        assertEquals("Updated Content", updatedComplaint.getComplaintContent());
    }

    @Test
    void testDeleteComplaint() throws Exception {
        // 기존 민원 저장
        Complaint savedComplaint = complaintRepository.save(
                Complaint.builder()
                        .member(savedMember)
                        .department(savedDepartment)
                        .complaintTitle("Test Title")
                        .complaintContent("Test Content")
                        .build()
        );

        // 삭제 요청 수행
        mockMvc.perform(delete("/complaint/" + savedComplaint.getComplaintSeq())
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());

        // DB 검증
        Optional<Complaint> deletedComplaint = complaintRepository.findById(savedComplaint.getComplaintSeq());
        assertTrue(deletedComplaint.isPresent());
        assertTrue(deletedComplaint.get().getIsDeleted());
    }

    @Test
    void testGetComplaint() throws Exception {
        // 기존 민원 저장
        Complaint savedComplaint = complaintRepository.save(
                Complaint.builder()
                        .member(savedMember)
                        .department(savedDepartment)
                        .complaintTitle("Test Title")
                        .complaintContent("Test Content")
                        .build()
        );

        // 조회 요청 수행
        mockMvc.perform(get("/complaint/" + savedComplaint.getComplaintSeq())
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.content").value("Test Content"));
    }
}