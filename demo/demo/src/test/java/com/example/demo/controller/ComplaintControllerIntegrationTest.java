package com.example.demo.controller;
import com.example.demo.entity.Complaint;
import com.example.demo.entity.User;
import com.example.demo.repository.ComplaintRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ComplaintService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Transactional
public class ComplaintControllerIntegrationTest {

    @InjectMocks
    private ComplaintService complaintService;

    @Mock
    private ComplaintRepository complaintRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testComplaintCreate() throws Exception {
        // User 객체 생성
        User user = new User(1L); // 생성자를 통한 userSeq 설정

        // Mock 설정
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(complaintRepository.save(org.mockito.ArgumentMatchers.any(Complaint.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // 전달된 객체를 반환

        // MockMultipartFile 사용
        MultipartFile mockFile = new MockMultipartFile(
                "file",
                "testfile.txt",
                "text/plain",
                "Test content".getBytes()
        );

        // Service 호출
        Complaint complaint = complaintService.complaintCreate(1L, "Test Title", "Test Content", mockFile);

        // 검증
        assertThat(complaint).isNotNull(); // 객체가 null이 아님을 확인
        assertThat(complaint.getComplaintTitle()).isEqualTo("Test Title");
        assertThat(complaint.getComplaintContent()).isEqualTo("Test Content");
    }

    @Test
    public void testComplaintUpdate() throws Exception {
        // 기존 민원 객체 생성
        User user = new User(1L); // 생성자를 통한 userSeq 설정
        Complaint existingComplaint = Complaint.builder()
                .complaintTitle("Old Title")
                .complaintContent("Old Content")
                .user(user)
                .build();

        // Mock 설정
        when(complaintRepository.findById(1L)).thenReturn(Optional.of(existingComplaint));
        when(complaintRepository.save(org.mockito.ArgumentMatchers.any(Complaint.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // 저장된 객체 반환

        // MockMultipartFile 사용
        MultipartFile mockFile = new MockMultipartFile(
                "file",
                "updatedfile.txt",
                "text/plain",
                "Updated content".getBytes()
        );

        // Service 호출 (민원 수정)
        Complaint updatedComplaint = complaintService.complaintUpdate(1L, "Updated Title", "Updated Content", mockFile);

        // 검증
        assertThat(updatedComplaint).isNotNull(); // 객체가 null이 아님을 확인
        assertThat(updatedComplaint.getComplaintTitle()).isEqualTo("Updated Title"); // 제목 확인
        assertThat(updatedComplaint.getComplaintContent()).isEqualTo("Updated Content"); // 내용 확인
    }
}