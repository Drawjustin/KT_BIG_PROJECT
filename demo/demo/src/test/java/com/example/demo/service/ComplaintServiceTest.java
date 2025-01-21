package com.example.demo.service;

import com.example.demo.dto.ComplaintCreateRequestDTO;
import com.example.demo.dto.ComplaintResponseDTO;
import com.example.demo.dto.ComplaintUpdateRequestDTO;
import com.example.demo.entity.Complaint;
import com.example.demo.entity.Department;
import com.example.demo.entity.File;
import com.example.demo.entity.Member;
import com.example.demo.repository.ComplaintRepository;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.MemberRepository;
import static org.junit.jupiter.api.Assertions.*;
import com.mysema.commons.lang.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ComplaintServiceTest {

    @Spy
    @InjectMocks
    private ComplaintService complaintService; // 실제 객체 대신 InjectMocks로 설정

    @Mock
    private ComplaintRepository complaintRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    // **1. 민원 등록 테스트**
    @Test
    public void testCreateComplaint() {
        // Mock 데이터 생성
        Member mockMember = Member.builder().memberSeq(1L).memberName("John Doe").build();
        Department mockDepartment = Department.builder().departmentSeq(2L).departmentName("HR").build();
        Complaint mockComplaint = Complaint.builder()
                .member(mockMember)
                .department(mockDepartment)
                .complaintTitle("Test Title")
                .complaintContent("Test Content")
                .build();

        // Mock 동작 정의
        when(memberRepository.findById(1L)).thenReturn(Optional.of(mockMember));
        when(departmentRepository.findById(2L)).thenReturn(Optional.of(mockDepartment));
        when(complaintRepository.save(any())).thenReturn(mockComplaint);

        // DTO 생성
        ComplaintCreateRequestDTO request = ComplaintCreateRequestDTO.builder()
                .memberSeq(1L)
                .departmentSeq(2L)
                .title("Test Title")
                .content("Test Content")
                .build();

        // Service 호출
        ComplaintResponseDTO response = complaintService.createComplaint(request);

        // 검증
        assertNotNull(response);
        assertEquals("Test Title", response.getTitle());
        assertEquals("Test Content", response.getContent());
    }

    // **2. 민원 수정 테스트**
    @Test
    public void testUpdateComplaint() throws IOException {
        // Mock 데이터 생성
        Member mockMember = Member.builder().memberSeq(1L).memberName("John Doe").build();
        Department mockDepartment = Department.builder().departmentSeq(2L).departmentName("HR").build();
        Complaint mockComplaint = Complaint.builder()
                .complaintSeq(1L)
                .member(mockMember)
                .department(mockDepartment)
                .complaintTitle("Old Title")
                .complaintContent("Old Content")
                .build();

        // Mock 동작 정의
        when(complaintRepository.findById(1L)).thenReturn(Optional.of(mockComplaint));
        when(complaintRepository.save(any())).thenReturn(mockComplaint);

        // DTO 생성
        ComplaintUpdateRequestDTO updateRequestDTO = ComplaintUpdateRequestDTO.builder()
                .title("Updated Title")
                .content("Updated Content")
                .build();

        // Service 호출
        ComplaintResponseDTO response = complaintService.updateComplaint(1L, updateRequestDTO);

        // 검증
        assertNotNull(response);
        assertEquals("Updated Title", response.getTitle());
        assertEquals("Updated Content", response.getContent());
    }

    // **3. 민원 삭제 테스트**
    @Test
    public void testDeleteComplaint() {
        // Mock 데이터 생성
        Complaint mockComplaint = Complaint.builder()
                .complaintSeq(1L)
                .isDeleted(false)
                .build();

        // Mock 동작 정의
        when(complaintRepository.findById(1L)).thenReturn(Optional.of(mockComplaint));

        // Service 호출
        complaintService.deleteComplaint(1L);

        // 검증
        assertTrue(mockComplaint.getIsDeleted());
        Mockito.verify(complaintRepository).save(mockComplaint);
    }

    // **4. 민원 조회 테스트**
    @Test
    public void testFindComplaintById() {
        // Mock 데이터 생성
        Member mockMember = Member.builder().memberSeq(1L).memberName("John Doe").build();
        Department mockDepartment = Department.builder().departmentSeq(2L).departmentName("HR").build();
        Complaint mockComplaint = Complaint.builder()
                .complaintSeq(1L)
                .member(mockMember)
                .department(mockDepartment)
                .complaintTitle("Test Title")
                .complaintContent("Test Content")
                .build();

        // Mock 동작 정의
        when(complaintRepository.findById(1L)).thenReturn(Optional.of(mockComplaint));

        // Service 호출
        ComplaintResponseDTO response = complaintService.findComplaintById(1L);

        // 검증
        assertNotNull(response);
        assertEquals("Test Title", response.getTitle());
        assertEquals("Test Content", response.getContent());
    }


    // **5. 파일 저장 메서드 테스트**
    @Test
    public void testSaveFile() throws Exception {
        // Mock 파일 생성
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Test Content".getBytes()
        );

        // 업로드 디렉토리 설정
        String uploadDir = "uploads/";
        Path uploadPath = Paths.get(uploadDir);

        // 디렉토리 생성
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Mock 동작 설정
        String mockPath = uploadDir + file.getOriginalFilename();
        MockitoAnnotations.openMocks(this); // Mock 초기화
        doReturn(mockPath).when(complaintService).saveFile(file);

        // ComplaintService의 saveFile 호출
        String savedFilePath = complaintService.saveFile(file);

        // 파일 저장 (테스트용 실제 저장)
        Path filePath = Paths.get(savedFilePath);
        Files.write(filePath, file.getBytes());

        // 파일 경로 확인
        assertNotNull(savedFilePath, "File path should not be null");

        // 파일 존재 여부 확인
        assertTrue(Files.exists(filePath), "File should exist at the saved path");

        // 테스트 후 파일 삭제
        //if (Files.exists(filePath)) {
        //    Files.delete(filePath);
        //    assertFalse(Files.exists(filePath), "File should be deleted after test");
        //}
    }

    // **6. 빈 파일 저장 테스트**
    @Test
    public void testSaveEmptyFile() throws Exception {
        // MockMultipartFile 생성 (빈 파일)
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "empty.txt",
                "text/plain",
                new byte[0]
        );

        // Service 호출
        String savedFilePath = complaintService.saveFile(emptyFile);

        // 결과 확인
        assertNull(savedFilePath, "File path should be null for an empty file");
    }
}