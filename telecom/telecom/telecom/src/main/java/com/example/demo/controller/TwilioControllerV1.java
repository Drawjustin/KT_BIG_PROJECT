package com.example.demo.controller;

import com.example.demo.config.TwilioConfig;
import com.example.demo.service.S3Service;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/twilio")
@RequiredArgsConstructor
public class TwilioControllerV1 {
    private final S3Service s3Service;
    private final TwilioConfig twilioConfig;
    @PostMapping(value = "/incoming", produces = MediaType.APPLICATION_XML_VALUE)
    public String handleIncomingCall() {
        return """
        <Response>
            <Say language="ko-KR" >안녕하세요</Say>
            <Gather action="/twilio/record" method="POST" numDigits="1"/>
        </Response>
        """;
    }

    @PostMapping(value = "/record", produces = MediaType.APPLICATION_XML_VALUE)
    public String startRecording(@RequestParam("Digits") String digits) {
        if ("3".equals(digits)) {
            return """
        <Response>
            <Say language="ko-KR">민원을 말씀해주세요</Say>
            <Gather input="speech"
                    action="/twilio/process-recording"
                    language="ko-KR"
                    speechModel="googlev2_telephony"
                    timeout="300"
                    speechTimeout="auto"/>
        </Response>
        """;
        }
        return handleIncomingCall();
    }

    @PostMapping(value = "/process-recording", produces = MediaType.APPLICATION_XML_VALUE)
    public String processRecording(
            @RequestParam(value = "SpeechResult", required = false) String speechResult,
            @RequestParam(value = "Confidence", required = false) String confidence,
            @RequestParam("CallSid") String callSid,
            // 모든 파라미터 확인을 위해 HttpServletRequest 추가
            HttpServletRequest request) {

        try {
            // 1. 모든 파라미터 로깅
            log.info("=== STT 결과 ===");
            log.info("Speech Result: {}", speechResult);
            log.info("Confidence: {}", confidence);
            log.info("Call SID: {}", callSid);

            // 2. 모든 요청 파라미터 로깅
            log.info("=== 전체 파라미터 ===");
            request.getParameterMap().forEach((key, value) -> {
                log.info("{}: {}", key, String.join(", ", value));
            });

            // 기존 로직 실행
            String savedUrl = s3Service.uploadText(speechResult, "transcripts/" + callSid + ".txt");
            return processComplaint(speechResult);

        } catch (Exception e) {
            log.error("Error processing recording", e);
            return """
        <Response>
            <Say language="ko-KR">처리 중 오류가 발생했습니다. 다시 시도해 주세요.</Say>
        </Response>
        """;
        }
    }
    private String sendToAIServer(String complaintText) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> request = Map.of("input", complaintText);
        return "sales"; // 임시 응답
    }

    private String determineTargetNumber(String aiResponse) {
        return switch (aiResponse) {
            case "sales" -> twilioConfig.getSalesId();
            default -> "+1234567893";
        };
    }
    private String processComplaint(String complaintText) {
        String aiResponse = sendToAIServer(complaintText);
        String targetNumber = determineTargetNumber(aiResponse);

        return """
        <Response>
            <Say language="ko-KR">담당 부서로 연결해드리겠습니다.</Say>
            <Dial callerId="%s">%s</Dial>
        </Response>
    """.formatted(twilioConfig.getCallerId(), targetNumber);
    }
}