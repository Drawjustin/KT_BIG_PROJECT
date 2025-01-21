//package com.example.demo.dto;
//
//import com.example.demo.entity.Complaint;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class ComplaintResponseDTO {
//    private Long complaintSeq;          // 민원 고유번호
//    private Long memberSeq;             // 등록자 고유번호
//    private String memberName;          // 등록자 이름
//    private Long departmentSeq;         // 부서 고유번호
//    private String departmentName;      // 부서 이름
//    private String title;               // 민원 제목
//    private String content;             // 민원 내용
//    private String filePath;            // 민원 파일 경로
//    private LocalDateTime createdAt;    // 생성 시간
//    private LocalDateTime updatedAt;    // 수정 시간
//    private boolean isDeleted;          // 삭제 여부
//
//    // Entity -> DTO 변환 메서드
//    public static ComplaintResponseDTO from(Complaint complaint) {
//        if (complaint == null || complaint.getMember() == null || complaint.getDepartment() == null) {
//            throw new IllegalArgumentException("Complaint or its relations are null");
//        }
//        return ComplaintResponseDTO.builder()
//                .complaintSeq(complaint.getComplaintSeq())
//                .memberSeq(complaint.getMember().getMemberSeq()) // Member 고유번호 접근
//                .memberName(complaint.getMember().getMemberName()) // Member 이름 접근
//                .departmentSeq(complaint.getDepartment().getDepartmentSeq()) // Department 고유번호 접근
//                .departmentName(complaint.getDepartment().getDepartmentName()) // Department 이름 접근
//                .title(complaint.getComplaintTitle())
//                .content(complaint.getComplaintContent())
//                .filePath(complaint.getComplaintFilePath())
//                .createdAt(complaint.getCreatedAt())
//                .updatedAt(complaint.getUpdatedAt())
//                .isDeleted(complaint.getIsDeleted()) // Boolean 타입 그대로 사용
//                .build();
//    }
//}
