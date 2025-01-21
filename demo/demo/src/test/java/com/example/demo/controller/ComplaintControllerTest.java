package com.example.demo.controller;

// Spring & Security imports
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Test imports
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.hamcrest.Matchers.*;

// Project imports
import com.example.demo.config.SecurityConfig;
import com.example.demo.dto.ComplaintResponseDTO;
import com.example.demo.dto.ComplaintListResponseDTO;
import com.example.demo.entity.Complaint;
import com.example.demo.entity.Department;
import com.example.demo.entity.Member;
import com.example.demo.service.ComplaintService;

// Java imports
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Import(SecurityConfig.class)
@WebMvcTest(ComplaintController.class)
@AutoConfigureMockMvc
public class ComplaintControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComplaintService complaintService;

    @Test
    public void testCreateComplaint() throws Exception {
        // Mock 데이터 설정
        ComplaintResponseDTO mockResponse = new ComplaintResponseDTO(
                1L,          // complaintSeq
                2L,          // memberSeq
                "John Doe",  // memberName
                3L,          // departmentSeq
                "HR",        // departmentName
                "Test Title",// complaintTitle
                "Test Content", // complaintContent
                "test/file/path", // complaintFilePath
                LocalDateTime.now(), // createdAt
                LocalDateTime.now(), // updatedAt
                false        // isDeleted
        );

        // Mock 행동 정의
        Mockito.when(complaintService.createComplaint(any()))
                .thenReturn(mockResponse);

        // 테스트 실행
        mockMvc.perform(
                        multipart("/complaint/create")
                                .file(new MockMultipartFile("file", "test.txt", "text/plain", "Test Content".getBytes()))
                                .param("memberSeq", "1")
                                .param("departmentSeq", "1")
                                .param("title", "Test Title")
                                .param("content", "Test Content")
                                .with(csrf())
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.complaintSeq").value(1))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.content").value("Test Content"))
                .andExpect(jsonPath("$.filePath").value("test/file/path"));
    }

    @Test
    public void testUpdateComplaint() throws Exception {
        Member mockMember = Member.builder()
                .memberSeq(2L)
                .memberName("John Doe")
                .build();

        Department mockDepartment = Department.builder()
                .departmentSeq(3L)
                .departmentName("HR")
                .build();

        Complaint mockComplaint = Complaint.builder()
                .complaintSeq(1L)
                .member(mockMember)
                .department(mockDepartment)
                .complaintTitle("Updated Title")
                .complaintContent("Updated Content")
                .complaintFilePath("/uploads/updated.txt")
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "updated.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Updated Content".getBytes()
        );

        Mockito.when(complaintService.updateComplaint(eq(1L), any()))
                .thenReturn(ComplaintResponseDTO.from(mockComplaint));

        mockMvc.perform(
                        multipart("/complaint/1/update")
                                .file(file)
                                .param("complaintTitle", "Updated Title")
                                .param("complaintContent", "Updated Content")
                                .with(csrf())
                                .with(request -> {
                                    request.setMethod("PUT");
                                    return request;
                                })
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Title")))
                .andExpect(jsonPath("$.content", is("Updated Content")))
                .andExpect(jsonPath("$.filePath", is("/uploads/updated.txt")));
    }

    @Test
    public void testDeleteComplaint() throws Exception {
        Mockito.doNothing().when(complaintService).deleteComplaint(1L);

        mockMvc.perform(delete("/complaint/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetComplaint() throws Exception {
        Member mockMember = Member.builder()
                .memberSeq(2L)
                .memberName("John Doe")
                .build();

        Department mockDepartment = Department.builder()
                .departmentSeq(3L)
                .departmentName("HR")
                .build();

        Complaint mockComplaint = Complaint.builder()
                .complaintSeq(1L)
                .member(mockMember)
                .department(mockDepartment)
                .complaintTitle("Test Title")
                .complaintContent("Test Content")
                .complaintFilePath("/uploads/test.txt")
                .build();

        Mockito.when(complaintService.findComplaintById(eq(1L)))
                .thenReturn(ComplaintResponseDTO.from(mockComplaint));

        mockMvc.perform(get("/complaint/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Test Title")))
                .andExpect(jsonPath("$.content", is("Test Content")))
                .andExpect(jsonPath("$.filePath", is("/uploads/test.txt")));
    }

    @Test
    public void testGetComplaints() throws Exception {
        Member mockMember = Member.builder()
                .memberSeq(2L)
                .memberName("John Doe")
                .build();

        Department mockDepartment = Department.builder()
                .departmentSeq(3L)
                .departmentName("HR")
                .build();

        Complaint c1 = Complaint.builder()
                .complaintSeq(1L)
                .member(mockMember)
                .department(mockDepartment)
                .complaintTitle("Title 1")
                .complaintContent("Content 1")
                .build();

        Complaint c2 = Complaint.builder()
                .complaintSeq(2L)
                .member(mockMember)
                .department(mockDepartment)
                .complaintTitle("Title 2")
                .complaintContent("Content 2")
                .build();

        List<Complaint> complaintList = List.of(c1, c2);

        Mockito.when(complaintService.findComplaintsByConditions(
                        any(), any(), any(), any(), any(), any()))
                .thenReturn(ComplaintListResponseDTO.fromList(complaintList));

        mockMvc.perform(
                        get("/complaint/list")
                                .param("page", "0")
                                .param("size", "10")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].title", is("Title 1")))
                .andExpect(jsonPath("$.content[0].content", is("Content 1")))
                .andExpect(jsonPath("$.content[1].title", is("Title 2")))
                .andExpect(jsonPath("$.content[1].content", is("Content 2")));
    }
}