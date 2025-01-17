package com.example.demo.service;

import com.example.demo.entity.Complaint;
import com.example.demo.entity.User;
import com.example.demo.repository.ComplaintRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ComplaintService {

    public final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;

    public ComplaintService(ComplaintRepository complaintRepository, UserRepository userRepository) {
        this.complaintRepository = complaintRepository;
        this.userRepository = userRepository;
    }

    // 민원 등록
    @Transactional
    public Complaint complaintCreate(Long userSeq, String title, String content, MultipartFile file) throws IOException {
        // 새로운 Complaint 객체를 Builder 패턴으로 생성
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다: " + userSeq));

        Complaint complaint = Complaint.builder()
                .user(user) // User 엔티티의 참조 (필요시 수정)
                .complaintTitle(title)
                .complaintContent(content)
                .complaintFilePath(file != null && !file.isEmpty() ? saveFile(file) : null)
                .build();

        return complaintRepository.save(complaint);
    }

    // 민원 수정
    @Transactional
    public Complaint complaintUpdate(Long id, String title, String content, MultipartFile file) throws IOException {
        // 기존 Complaint 객체 조회
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 민원을 찾을 수 없습니다: " + id));

        // 업데이트 메서드 호출
        complaint.updateComplaint(
                title,
                content,
                file != null && !file.isEmpty() ? saveFile(file) : complaint.getComplaintFilePath()
        );

        return complaintRepository.save(complaint);
    }

    // 파일 저장
    private String saveFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        String uploadDir = "./uploads"; // 상대 경로
        Path filePath = Paths.get(uploadDir, fileName);

        // 디렉토리 존재 여부 확인 및 생성
        if (!Files.exists(filePath.getParent())) {
            Files.createDirectories(filePath.getParent());
        }

        // 파일 저장
        file.transferTo(filePath.toFile());
        return filePath.toString();
    }

    // 민원 조회 (단건)


}
