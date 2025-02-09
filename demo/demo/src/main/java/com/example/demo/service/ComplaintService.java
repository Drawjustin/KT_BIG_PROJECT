package com.example.demo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.demo.config.RestTemplateConfig;
import com.example.demo.config.S3Config;
import com.example.demo.dto.*;
import com.example.demo.dto.ai.*;
import com.example.demo.entity.*;
import com.example.demo.exception.RestApiException;
import com.example.demo.repository.*;
import com.example.demo.utils.ExceptionHandlerUtil;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.demo.exception.ErrorCodeCustom.*;


@Service
@Transactional
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final RestTemplateConfig restTemplateConfig;
    private final DepartmentRepository departmentRepository;
    private final AmazonS3 amazonS3;
    private final DistrictRepository districtRepository;
    @Value("${aws.s3.directory}")
    private String directory;

    @Value("${aws.s3.bucket}")
    private String bucket;
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OtherCreateRequestDTO {
        private Team team;
        private Byte Count;
        private Boolean isBad;
        private TextSummaryResponse textSummaryResponse;
        private String filePath;
    }
    // 민원 등록
    @Transactional
    public ComplaintCreateResponseDTO createComplaint(CustomUserDetails userDetails, ComplaintCreateRequestDTO request) {
        // 민원 요약
        TextSummaryResponse textSummaryResponse = getTextSummary(request.getContent());

        System.out.println("textSummaryResponse = " + textSummaryResponse);

        // 부서 분류
        String department = predictDepartment(textSummaryResponse.getCombined()).replace("\"", "");
        System.out.println("department = " + department);

        // 팀 분류
        PredictTeamResponse predictTeamResponse = predictTeam(department, textSummaryResponse.getCombined());

        System.out.println("userDetails.getMember(): " + userDetails.getMember());

        Optional<Member> byId = memberRepository.findById(userDetails.getMember().getMemberSeq());
        System.out.println("byId present?: " + byId.isPresent());

        Optional<District> byDistrictName = districtRepository.findByDistrictName(predictTeamResponse.get구분());
        System.out.println("구분: " + predictTeamResponse.get구분());
        System.out.println("byDistrictName present?: " + byDistrictName.isPresent());

// 이 부분에서 에러가 날 가능성이 높음 - byDistrictName이 비어있는데 get() 호출
        Optional<Department> byDepartmentNameAndDistrictSeq = departmentRepository.findByDepartmentNameAndDistrictSeq(
                department,
                byDistrictName.get().getDistrictSeq()  // 여기서 NoSuchElementException 발생 가능
        );

        Optional<Team> byTeam = teamRepository.findByTeamNameAndDepartmentDepartmentSeq(
                predictTeamResponse.get팀(),
                byDepartmentNameAndDistrictSeq.get().getDepartmentSeq()
        );
        // 악성 민원 판단
        Integer i = predictMalcs(new MalcsRequest(textSummaryResponse.getSummary()));
        boolean isBad = i == 1;

        // 반복 민원 판단
        ArrayList<PastComplaint> pastComplaints = new ArrayList<>();
        List<String> recentComplaintSummariesByMemberSeq = complaintRepository.findRecentComplaintSummariesByMemberSeq(userDetails.getMember().getMemberSeq(), LocalDateTime.now().minusDays(10));
        for (String summary : recentComplaintSummariesByMemberSeq) {
            pastComplaints.add(new PastComplaint(summary));
        }
        Integer repeatCount = !pastComplaints.isEmpty() ? getRepeatCount(textSummaryResponse.getSummary(), pastComplaints) : 0;


        String filePath = request.getFile() != null ? saveFile(request.getFile()) : null;

        OtherCreateRequestDTO otherCreateRequestDTO = new OtherCreateRequestDTO(byTeam.get(), repeatCount.byteValue(), isBad, textSummaryResponse,filePath);
        Complaint complaint = buildComplaint(userDetails,request,otherCreateRequestDTO);
        return saveAndCreateResponse(complaint);
    }
    // 민원 업데이트
    @Transactional
    public String updateComplaint(Long id, ComplaintUpdateRequestDTO request) {
        Complaint complaint = findComplaintOrThrow(id);
        updateComplaintDetails(complaint, request);
        return "수정이 잘 되었습니다";
    }
    // 민원 삭제
    @Transactional
    public void deleteComplaint(Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RestApiException(NO_COMPLAINT));
        complaint.markAsDeleted();
    }

    // 민원 조회 (단건)
    @Transactional
    public ComplaintResponseDTO findComplaintById(Long id) {
        return complaintRepository.getComplaintById(id);
    }

    // 민원 페이지 조회 (여러건)
    @Transactional
    public Page<ComplaintResponseDTO> findComplaintsByConditions(ComplaintSearchCondition condition, Pageable pageable){
        PageRequest page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return complaintRepository.getComplaints(condition,page);
    }


    private Complaint buildComplaint(CustomUserDetails memberDetails, ComplaintCreateRequestDTO request, OtherCreateRequestDTO otherCreateRequestDTO) {
        return Complaint.builder()
                .member(memberDetails.getMember())//멤버넣고
                .team(otherCreateRequestDTO.getTeam())//ai팀찾기
                .complaintTitle(request.getTitle())
                .complaintSummary(otherCreateRequestDTO.textSummaryResponse.getSummary()) // ai민원ㅇ요약
                .complaintCombined(otherCreateRequestDTO.textSummaryResponse.getCombined()) // ai민원요약2
                .isBad(otherCreateRequestDTO.getIsBad())//ai악성민원여부 확인
                .complaintCount(otherCreateRequestDTO.getCount()) // ai 반복민원갯수 판단
                .isAnswered(false)
                .complaintContent(request.getContent())
                .complaintFilePath(otherCreateRequestDTO.getFilePath()) // s3불러오기
                .build();
    }

    public PredictTeamResponse predictTeam(String department, String text) {
        try {
            // Request body 생성
            PredictTeamRequest request = new PredictTeamRequest(department, text);

            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // HTTP 엔티티 생성
            HttpEntity<PredictTeamRequest> entity = new HttpEntity<>(request, headers);

            // API 호출
            ResponseEntity<PredictTeamResponse> response = restTemplateConfig.restTemplate().exchange(
                    restTemplateConfig.getPredictTeamUrl(),
                    HttpMethod.POST,
                    entity,
                    PredictTeamResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to predict team");
            }
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to call predict team API", e);
        }
    }
    public TextSummaryResponse getTextSummary(String text) {
        try {
            TextSummaryRequest textSummaryRequest = new TextSummaryRequest(text);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<TextSummaryRequest> entity = new HttpEntity<>(textSummaryRequest, headers);

            ResponseEntity<TextSummaryResponse> response = restTemplateConfig.restTemplate().exchange(
                    restTemplateConfig.getGetTextSummaryUrl(),
                    HttpMethod.POST,
                    entity,
                    TextSummaryResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to get text summary");
            }
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to call text summary API", e);
        }
    }
    public String predictDepartment(String text) {
        try {
            DepartmentRequest request = new DepartmentRequest(text);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<DepartmentRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<String> response = restTemplateConfig.restTemplate().exchange(
                    restTemplateConfig.getPredictDepartmentUrl(),
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to predict department");
            }
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to call predict department API", e);
        }
    }
    public Integer getRepeatCount(String text, List<PastComplaint> pastComplaints) {
        try {
            RepeatCountRequest request = new RepeatCountRequest(text, pastComplaints);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<RepeatCountRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<Integer> response = restTemplateConfig.restTemplate().exchange(
                    restTemplateConfig.getRepeatCountUrl(),
                    HttpMethod.POST,
                    entity,
                    Integer.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to get repeat count");
            }
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to call repeat count API", e);
        }
    }
    public Integer predictMalcs(MalcsRequest summary) {
        try {
            MalcsRequest request = new MalcsRequest(summary.getText());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<MalcsRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<Integer> response = restTemplateConfig.restTemplate().exchange(
                    restTemplateConfig.getPredictMalcsUrl(),
                    HttpMethod.POST,
                    entity,
                    Integer.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to predict malcs");
            }
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to call predict malcs API", e);
        }
    }
    private Member findMemberOrThrow(Long memberSeq) {
        return memberRepository.findById(memberSeq)
                .orElseThrow(() -> new RestApiException(NO_MEMBER));
    }

    private Team findTeamOrThrow(Long teamSeq) {
        return teamRepository.findById(teamSeq)
                .orElseThrow(() -> new RestApiException(NO_DEPARTMENT));
    }

    private ComplaintCreateResponseDTO saveAndCreateResponse(Complaint complaint) {
        Long complaintSeq = complaintRepository.save(complaint).getComplaintSeq();
        return ComplaintCreateResponseDTO.builder()
                .complaintSeq(complaintSeq)
                .build();
    }

    public String saveFile(MultipartFile file) {
        return ExceptionHandlerUtil.executeWithIOException(
                () -> saveAWSFile(file),
                new RestApiException(FAIL_SAVED_FILE)
        );
    }

    private String saveAWSFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String uniqueFileName = generateUniqueFileName(originalFilename);
        String s3Key = directory + "/" + uniqueFileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3.putObject(bucket, s3Key, file.getInputStream(), metadata);

        return amazonS3.getUrl(bucket, s3Key).toString();
    }

    private Complaint findComplaintOrThrow(Long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new RestApiException(NO_COMPLAINT));
    }



    private void updateComplaintDetails(Complaint complaint, ComplaintUpdateRequestDTO request) {
        complaint.updateComplaint(
                request.getTitle(),
                request.getContent(),
                saveFile(request.getFile())
        );
    }

    private String generateUniqueFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String extension = StringUtils.getFilenameExtension(originalFilename);
        return uuid + (extension != null ? "." + extension : "");
    }
}
