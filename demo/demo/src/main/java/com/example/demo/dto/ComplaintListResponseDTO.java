//package com.example.demo.dto;
//
//import com.example.demo.entity.Complaint;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.springframework.data.domain.Page;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class ComplaintListResponseDTO {
//    private Long complaintSeq;          // 민원 고유번호
//    private String memberName;          // 작성자 이름
//    private String departmentName;      // 부서 이름
//    private String title;               // 민원 제목
//    private String content;
//    private boolean hasFile;            // 파일 존재 여부
//    private LocalDateTime createdAt;    // 생성 시간
//
//    // 페이징 정보 (다건 조회용)
//    private int totalPages;             // 총 페이지 수
//    private long totalElements;         // 총 요소 수
//    private boolean hasNext;            // 다음 페이지 존재 여부
//    private boolean hasPrevious;        // 이전 페이지 존재 여부
//
//    // Entity -> DTO 변환 메서드
//    public static ComplaintListResponseDTO from(Complaint complaint) {
//        return ComplaintListResponseDTO.builder()
//                .complaintSeq(complaint.getComplaintSeq())  // 민원 고유번호
//                .memberName(complaint.getMember().getMemberName()) // 작성자 이름
//                .departmentName(complaint.getDepartment().getDepartmentName()) // 부서 이름
//                .title(complaint.getComplaintTitle()) // 제목
//                .content(complaint.getComplaintContent())
//                .hasFile(complaint.getComplaintFilePath() != null) // 파일 경로 존재 여부
//                .createdAt(complaint.getCreatedAt()) // 생성 시간
//                .build();
//    }
//
//    // Page<Entity> -> List<DTO> 변환 메서드
//    public static List<ComplaintListResponseDTO> fromPage(Page<Complaint> page) {
//        return page.getContent().stream()
//                .map(ComplaintListResponseDTO::from) // 각 Complaint 엔터티를 DTO로 변환
//                .collect(Collectors.toList());
//    }
//
//    // Page<Entity> -> DTO + 페이징 정보 변환 메서드
//    public static PaginatedResponse fromPageWithPagination(Page<Complaint> page) {
//        List<ComplaintListResponseDTO> content = page.getContent().stream()
//                .map(ComplaintListResponseDTO::from) // Complaint -> DTO 변환
//                .collect(Collectors.toList());
//
//        return PaginatedResponse.builder()
//                .content(content)
//                .totalPages(page.getTotalPages())
//                .totalElements(page.getTotalElements())
//                .hasNext(page.hasNext())
//                .hasPrevious(page.hasPrevious())
//                .build();
//    }
//
//    @Getter
//    @Builder
//    // 민원이 많을 경우 페이징 정보와 함께 데이터를 클라이언트에 전달
//    public static class PaginatedResponse {
//        private List<ComplaintListResponseDTO> content; // 민원 리스트
//        private int totalPages;                         // 총 페이지 수
//        private long totalElements;                     // 총 요소 수
//        private boolean hasNext;                        // 다음 페이지 존재 여부
//        private boolean hasPrevious;                    // 이전 페이지 존재 여부
//    }
//    public static PaginatedResponse fromList(List<Complaint> complaints) {
//        List<ComplaintListResponseDTO> responseList = complaints.stream()
//                .map(ComplaintListResponseDTO::from) // Complaint -> ComplaintListResponseDTO 변환
//                .toList();
//
//        return PaginatedResponse.builder()
//                .content(responseList)
//                .totalPages(1) // 전체를 한 페이지로 가정
//                .totalElements(responseList.size())
//                .hasNext(false)
//                .hasPrevious(false)
//                .build();
//    }
//}