package com.example.demo.service;

import com.example.demo.config.TwilioConfig;
import com.example.demo.entity.Telecom;
import com.example.demo.repository.TelecomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TelecomService {
    private final S3Service s3Service;
    private final STTService sttService;
    private final TwilioConfig twilioConfig;
    private final TelecomRepository telecomRepository;
    public String recordingProcess(String recordingUrl, String fromNumber){
        try{
            System.out.println("TelecomService.recordingProcess");
            System.out.println("recordingUrl = " + recordingUrl);
            System.out.println("fromNumber = " + fromNumber);
            Thread.sleep(2000);
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

            telecomRepository.save(Telecom.builder().telecomContent(transcribedText)
                    .telecomCount((byte) 0)
                    .telecomFilePath(uploadAudio)
                    .isComplain(false)
                    .build());
//            complaintRepository.save(new Complaint(fromNumber, transcribedText, audioFileName));

        // 5. AI 처리
        String department = sendToAIServer(transcribedText);
        String encodedDepartment = URLEncoder.encode(department, StandardCharsets.UTF_8);
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
               """.formatted(transcribedText, department, encodedDepartment);
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
    private String sendToAIServer(String complaintText) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> request = Map.of("input", complaintText);
        return "sales"; // 임시 응답
    }

    public String determineTargetNumber(String encodedDepartment, String digits) {
        // URL 디코딩
        String department = URLDecoder.decode(encodedDepartment, StandardCharsets.UTF_8);

        System.out.println("department = " + department);
        if ("1".equals(digits)) {
            String targetNumber = determineTargetNumber(department);
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
    private String determineTargetNumber(String aiResponse) {
        return switch (aiResponse) {
            case "sales" -> twilioConfig.getSalesId();
            default -> "+1234567893";
        };
    }
}
