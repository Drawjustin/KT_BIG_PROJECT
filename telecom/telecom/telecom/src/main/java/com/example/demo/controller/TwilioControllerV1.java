package com.example.demo.controller;

import com.example.demo.config.TwilioConfig;
import com.example.demo.service.S3Service;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
       <Play digits="9"/>
        <Pause length="0.5"/>
        <Play digits="1"/>
            <Say language="ko-KR">안녕하세요 창원시 민원 콜센터입니다.
            민원을 넣으시려면 1번을 눌러주세요.</Say>
            <Gather action="/twilio/record" method="POST" numDigits="1"/>
        </Response>
        """;
    }

    @PostMapping(value = "/record", produces = MediaType.APPLICATION_XML_VALUE)
    public String startRecording(@RequestParam("Digits") String digits) {
        if (!"1".equals(digits)) {
            return handleIncomingCall();
        }

        return """
        <Response>
            <Say language="ko-KR">안녕하십니까. 민원상담 자동응답 시스템입니다. 
        접수하실 민원 내용을 말씀해 주시면 담당 부서로 전달하여 처리해 드리겠습니다.
        할말이 끝난뒤에는 2번을 눌러주세요. 
        삐 소리 후 말씀해 주시기 바랍니다.</Say>
            <Pause length="1"/>
            <Play tone="dtmf-1"/>
            <Gather input="speech dtmf"
                    action="/twilio/confirm-recording"
                    language="ko-KR"
                    numDigits="1"
                    speechModel="googlev2_telephony"
                    timeout="300"
                    speechTimeout="5"/>
            <Redirect method="POST">/twilio/record?Digits=1</Redirect>
        </Response>
        """;
    }

    @PostMapping(value = "/confirm-recording", produces = MediaType.APPLICATION_XML_VALUE)
    public String confirmRecording(
            @RequestParam(value = "SpeechResult", required = false) String speechResult,
            @RequestParam("CallSid") String callSid
    ) {
        if (speechResult == null) {
            return """
        <Response>
            <Say language="ko-KR">죄송합니다. 음성 인식에 실패했습니다. 다시 시도해주세요.</Say>
            <Redirect method="POST">/twilio/record?Digits=1</Redirect>
        </Response>
        """;
        }

        String department = sendToAIServer(speechResult);

        // URL 인코딩
        String encodedDepartment =  URLEncoder.encode(department, StandardCharsets.UTF_8);

        s3Service.uploadText(speechResult, "transcripts/" + callSid + ".txt");
        log.info("민원 내용: {}, 배정 부서: {}", speechResult, department);

        return """
    <Response>
        <Say language="ko-KR">고객님께서 말씀하신 민원 내용은 다음과 같습니다.</Say>
        <Say language="ko-KR">%s 민원, 이에 적합한 부서는 %s 부서로 판단됩니다.</Say>
        <Say language="ko-KR">해당 부서로 연결에 동의하시면 1번,
        다시 말씀하시려면 2번을 눌러주세요.</Say>
        <Gather input="dtmf"
                action="/twilio/final-process/%s"
                numDigits="1"
                timeout="5"/>
    </Response>
    """.formatted(speechResult, department, encodedDepartment);
    }

    @PostMapping(value = "/final-process/{department}", produces = MediaType.APPLICATION_XML_VALUE)
    public String finalProcess(
            @PathVariable("department") String encodedDepartment,
            @RequestParam("Digits") String digits
    ) {
        // URL 디코딩
        String department = URLDecoder.decode(encodedDepartment, StandardCharsets.UTF_8);

        System.out.println("department = " + department);
        if ("1".equals(digits)) {
            String targetNumber = determineTargetNumber(department);
            log.info("연결 대상 번호: {}", targetNumber);
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
        <Redirect method="POST">/twilio/record?Digits=1</Redirect>
    </Response>
    """;
    }

    private String sendToAIServer(String complaintText) {
        // 실제 AI 서버 연동 로직으로 교체 필요
        return "창원시청 행정부";
    }

    private String determineTargetNumber(String department) {
        return switch (department) {
            case "창원시청 행정부" -> twilioConfig.getSalesId();
            default -> "+1234567893";
        };
    }
}