package com.example.demo.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/twilio")
public class TwilioController {

    @PostMapping(value = "/incoming", produces = MediaType.APPLICATION_XML_VALUE)
    public String handleIncomingCall() {
        return """
            <Response>
                <Say>Welcome to our service. Please press any key.</Say>
                <Gather action="/twilio/process-input" method="POST" numDigits="1">
                    <Say>Please press a key now.</Say>
                </Gather>
                <Say>No input received. Goodbye!</Say>
            </Response>
            """;
    }
    @PostMapping(value = "/process-input", produces = MediaType.APPLICATION_XML_VALUE)
    public String processUserInput(@RequestParam("Digits") String digits) {
        // AI 서버와 연동해 결과 받기
        String aiResponse = sendToAIServer(digits);

        // AI 응답을 기반으로 라우팅
        String targetNumber = determineTargetNumber(aiResponse);
        String callerId = "+"; // 본인 인증된 한국 전화번호
        return """
            <Response>
                <Say>Connecting your call now.</Say>
                <Dial callerId="%s">%s</Dial>
            </Response>
            """.formatted(callerId,targetNumber);
    }

    private String sendToAIServer(String digits) {
        // AI 서버로 HTTP 요청 전송
        // (예제: AI 서버에 POST 요청으로 사용자 입력 전송)
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> request = Map.of("input", digits);
      /*  ResponseEntity<String> response = restTemplate.postForEntity(
                "https://your-ai-server.com/analyze", request, String.class);
        return response.getBody();
       */
        return "sales";
    }

    private String determineTargetNumber(String aiResponse) {
        // AI 응답 기반으로 적절한 전화번호 선택
        return switch (aiResponse) {
            case "sales" -> "+";
            case "support" -> "+12345678902";
            case "billing" -> "+12345678903";
            default -> "+12345678904";
        };
    }

}

