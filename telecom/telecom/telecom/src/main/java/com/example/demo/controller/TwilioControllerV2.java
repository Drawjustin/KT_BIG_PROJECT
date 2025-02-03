//package com.example.demo.controller;
//
//import com.example.demo.config.TwilioConfig;
//import com.example.demo.service.S3Service;
//import com.example.demo.service.STTService;
//import com.twilio.Twilio;
//import com.twilio.rest.api.v2010.account.Recording;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.*;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Map;
//
//@Slf4j
//@RestController
//@RequestMapping("/twilio")
//@RequiredArgsConstructor
//public class TwilioControllerV2 {
//    private final S3Service s3Service;
//    private final STTService sttService;
//    private final TwilioConfig twilioConfig;
//        @PostMapping(value = "/incoming", produces = MediaType.APPLICATION_XML_VALUE)
//    public String handleIncomingCall() {
//        return """
//        <Response>
//            <Say language="ko-KR" >안녕하세요</Say>
//            <Gather action="/twilio/record" method="POST" numDigits="1"/>
//        </Response>
//        """;
//    }
//    @PostMapping(value = "/record", produces = MediaType.APPLICATION_XML_VALUE)
//    public String startRecording(@RequestParam("Digits") String digits) {
//        if ("3".equals(digits)) {
//            return """
//            <Response>
//                <Say language="ko-KR">민원을 말씀해주세요.</Say>
//                <Record action="/twilio/process-recording"
//                        maxLength="300"
//                        finishOnKey="4"
//                        trim="trim-silence"/>
//            </Response>
//            """;
//        }
//        return handleIncomingCall();
//    }
//
//    @PostMapping(value = "/process-recording", produces = MediaType.APPLICATION_XML_VALUE)
//    public String processRecording(@RequestParam Map<String, String> allParams,
//                                   @RequestParam("CallSid") String callSid) {
//        try {
//            Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
//            Thread.sleep(2000);  // 2초 대기
//            String recordingSid = allParams.get("RecordingSid");
//            Recording recording = Recording.fetcher(recordingSid).fetch();
//
//            // MP3 대신 WAV 형식으로 요청
//            String mediaUrl = recording.getMediaUrl().toString() + ".wav";
//            log.info("Media URL: {}", mediaUrl);
//            log.info("Recording duration: {} seconds", recording.getDuration());
//
//            RestTemplate restTemplate = new RestTemplate();
//            HttpHeaders headers = new HttpHeaders();
//            headers.setBasicAuth(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
//            HttpEntity<String> entity = new HttpEntity<>(headers);
//            ResponseEntity<byte[]> response = restTemplate.exchange(
//                    mediaUrl,
//                    HttpMethod.GET,
//                    entity,
//                    byte[].class
//            );
//
//            byte[] recordingData = response.getBody();
//            log.info("Recording downloaded successfully. Size: {} bytes",
//                    recordingData != null ? recordingData.length : 0);
//
//            String speechResult = sttService.convertToText(recordingData, 8000);
//            log.info("STT Result: {}", speechResult);
//
//            String savedUrl = s3Service.uploadText(speechResult, "transcripts/" + callSid + ".txt");
//            return processComplaint(speechResult);
//
//        } catch (Exception e) {
//            log.error("Error details: ", e);
//            return """
//        <Response>
//            <Say language="ko-KR">처리 중 오류가 발생했습니다. 다시 시도해 주세요.</Say>
//        </Response>
//        """;
//        }
//    }
//
//
//
//
//        private String sendToAIServer(String complaintText) {
//        RestTemplate restTemplate = new RestTemplate();
//        Map<String, String> request = Map.of("input", complaintText);
//        return "sales"; // 임시 응답
//    }
//
//    private String determineTargetNumber(String aiResponse) {
//        return switch (aiResponse) {
//            case "sales" -> twilioConfig.getSalesId();
//            default -> "+1234567893";
//        };
//    }
//        private String processComplaint(String complaintText) {
//        String aiResponse = sendToAIServer(complaintText);
//        String targetNumber = determineTargetNumber(aiResponse);
//
//        return """
//        <Response>
//            <Say language="ko-KR">담당 부서로 연결해드리겠습니다.</Say>
//            <Dial callerId="%s">%s</Dial>
//        </Response>
//    """.formatted(twilioConfig.getCallerId(), targetNumber);
//    }
//}