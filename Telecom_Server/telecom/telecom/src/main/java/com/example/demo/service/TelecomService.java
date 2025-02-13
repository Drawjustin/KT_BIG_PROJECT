package com.example.demo.service;

import com.example.demo.config.TwilioConfig;
import com.example.demo.entity.Department;
import com.example.demo.entity.District;
import com.example.demo.entity.Telecom;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.DistrictRepository;
import com.example.demo.repository.TelecomRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TelecomService {
    private final S3Service s3Service;
    private final STTService sttService;
    private final TwilioConfig twilioConfig;
    private final TelecomRepository telecomRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DepartmentRepository departmentRepository;
    private final DistrictRepository districtRepository;
    @Value("${model.text-summary.url}")
    private String summaryAIServer;
    @Value("${model.department.url}")
    private String departmentAIServer;
    @Value("${model.team.url}")
    private String teamAIServer;
    @Value("${model.predictMalcs_by_module.url}")
    private String malcsAIServer;

    public String recordingProcess(String recordingUrl, String fromNumber){
        try{
            System.out.println("TelecomService.recordingProcess");
            System.out.println("recordingUrl = " + recordingUrl);
            System.out.println("fromNumber = " + fromNumber);
            Thread.sleep(5000);
            // 1. 녹음 파일 다운로드
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
            ResponseEntity<byte[]> response = restTemplate.exchange(
                recordingUrl + ".wav",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                byte[].class
            );

            System.out.println("TelecomService.recordingProcess");
            // 2. STT 변환
            String transcribedText = sttService.convertToText(response.getBody(), 8000);
            System.out.println("TelecomService.recordingProcess");

            // 3. 음성 파일 S3 저장
            String audioFileName = String.format(
                "%s/%s_%s.wav",
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
                new SimpleDateFormat("HHmmss").format(new Date()),
                fromNumber
        );
            System.out.println("TelecomService.recordingProcess");
            String uploadAudio = s3Service.uploadAudio(recordingUrl, audioFileName);

            // 4. DB 저장


        // 5. AI 처리
        System.out.println("transcribedText = " + transcribedText);
        Map<String,String> summaryInfo = sendToAIServer(transcribedText);
        String department = departmentAIServer(summaryInfo.get("combined"));
        Map<String, String> teamInfo = teamAIServer(department,summaryInfo.get("combined"));

        District district = districtRepository.findByDistrictName(teamInfo.get("구분")).get();
            Department departmentResult = departmentRepository.findByDepartmentNameAndDistrictSeq(teamInfo.get("부서"), district.getDistrictSeq()).get();
            Boolean isComplainState = Optional.ofNullable(MalcAIServer(transcribedText))
                .map(result -> result == 1)
                .orElse(false);  // AI 서버 호출 실패시 기본값 false

            telecomRepository.save(Telecom.builder().telecomContent(transcribedText)
                .telecomCount((byte) 0)
                .telecomFilePath(uploadAudio)
                .isComplain(isComplainState)
                .department(departmentResult)
                .build());
        // Map을 JSON 문자열로 변환 후 인코딩
            String encodedTeamInfo = URLEncoder.encode(objectMapper.writeValueAsString(teamInfo), StandardCharsets.UTF_8);
            return """
            <Response>
                <Say language="ko-KR">고객님께서 말씀하신 민원 내용은 다음과 같습니다.</Say>
                <Say language="ko-KR">%s 민원, 이에 적합한 부서는 %s 부서로 판단됩니다.</Say>
                <Say language="ko-KR">해당 부서로 연결에 동의하시면 1번,
                다시 말씀하시려면 2번을 눌러주세요.</Say>
                <Gather input="dtmf"
                        action="/telecoms/final-process/%s"
                        numDigits="1"
                        timeout="5"/>
            </Response>
            """.formatted(summaryInfo.get("summary"), teamInfo.get("부서"), encodedTeamInfo);
        }
        catch (Exception e) {
            e.printStackTrace();
            return """
               <Response>
                   <Say language="ko-KR">처리 중 오류가 발생했습니다. 다시 시도해주세요.</Say>
                   <Redirect method="POST">/telecoms/record?Digits=1</Redirect>
               </Response>
               """;

        }
    }

    private Map teamAIServer(String department, String combinded) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        // 로깅 추가
        System.out.println("department = " + department);
        System.out.println("combinded = " + combinded);

        // 요청 바디 생성 - LinkedHashMap 사용
        Map<String, String> requestBody = new LinkedHashMap<>();
        requestBody.put("class_department", department);
        requestBody.put("text", combinded);

        // POST 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(requestBody);
        System.out.println("Sending JSON: " + jsonBody);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    teamAIServer,
                    entity,
                    Map.class
            );

            System.out.println("Response status: " + response.getStatusCode());
            System.out.println("Response body: " + response.getBody());
            System.out.println("성공!");
            return response.getBody();

        } catch (RestClientException e) {
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    private Integer MalcAIServer(String text) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> requestBody = new LinkedHashMap<>();
        requestBody.put("text", text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Integer> response = restTemplate.postForEntity(
                    malcsAIServer,
                    entity,
                    Integer.class
            );

            return response.getBody();

        } catch (RestClientException e) {
            return null;
        }
    }
    private String departmentAIServer(String summaryText) {
        RestTemplate restTemplate = new RestTemplate();
        // 요청과 응답 모두 UTF-8로 설정
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        restTemplate.getMessageConverters().set(0, new ByteArrayHttpMessageConverter());

        // 요청 바디 생성
        Map<String, String> requestBody = Map.of("text", summaryText);

        // POST 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HTTP 엔티티 생성 (헤더 + 바디)
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    departmentAIServer,
                    entity,
                    String.class
            );

            if (response.getBody() != null) {
                return response.getBody().replace("\"", "");  // 쌍따옴표 제거
            }

            return "Failed to get response";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    private Map sendToAIServer(String complaintText) {
        RestTemplate restTemplate = new RestTemplate();
        // 요청과 응답 모두 UTF-8로 설정
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        restTemplate.getMessageConverters().set(0, new ByteArrayHttpMessageConverter());

        // 요청 바디 생성
        Map<String, String> requestBody = Map.of("text", complaintText);

        // POST 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HTTP 엔티티 생성 (헤더 + 바디)
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    summaryAIServer,
                    entity,
                    Map.class
            );

            if (response.getBody() != null) {
                System.out.println("response.getBody().get(\"summary\") = " + response.getBody().get("summary"));
                System.out.println("response.getBody().get(\"combined\") = " + response.getBody().get("combined"));
                return response.getBody();
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String determineTargetNumber(String encodedTeamInfo, String digits) throws JsonProcessingException {
        Map<String, String> teamInfo = objectMapper.readValue(
                URLDecoder.decode(encodedTeamInfo, StandardCharsets.UTF_8),
                new TypeReference<Map<String, String>>() {}
        );

        if ("1".equals(digits)) {
            String targetNumber = determineTargetNumber(teamInfo);
            return """
        <Response>
            <Say language="ko-KR">담당 부서로 연결해드리겠습니다.</Say>
            <Dial callerId="%s">%s</Dial>
        </Response>
        """.formatted(twilioConfig.getCallerId(), targetNumber);
        }

        System.out.println("TwilioControllerV1.finalProcess");
        return """
        <Response>
            <Redirect method="POST">/telecoms/record?Digits=1</Redirect>
        </Response>
        """;
    }
    private String determineTargetNumber(Map<String, String> teamInfo) {
        return teamInfo.get("전화번호") != null ? twilioConfig.getSalesId() : "";
//        return switch (aiResponse) {
//            case "sales" -> twilioConfig.getSalesId();
//            default -> "+1234567893";
//        };
    }
}
