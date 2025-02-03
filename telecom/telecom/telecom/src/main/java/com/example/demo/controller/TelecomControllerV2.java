package com.example.demo.controller;

import com.example.demo.config.TwilioConfig;
import com.example.demo.service.S3Service;
import com.example.demo.service.STTService;
import com.example.demo.service.TelecomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/telecoms")
@RequiredArgsConstructor
public class TelecomControllerV2 {

    private final TelecomService telecomService;
    @PostMapping(value = "/incoming", produces = MediaType.APPLICATION_XML_VALUE)
    public String handleIncomingCall() {
        return """
        <Response>
            <Say language="ko-KR" >안녕하세요</Say>
            <Gather action="/telecoms/record" method="POST" numDigits="1"/>
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
       할말이 끝난뒤에는 2번을 눌러주세요.</Say>
           <Record 
               action="/telecoms/process-recording"
               maxLength="300"
               recordingStatusCallback="/telecoms/recording-status"
               recordingStatusCallbackMethod="POST"
               trim="trim-silence"/>
       </Response>
       """;
    }
    @PostMapping(value = "/process-recording", produces = MediaType.APPLICATION_XML_VALUE)
    public String processRecording(
            @RequestParam("RecordingUrl") String recordingUrl,
            @RequestParam("From") String fromNumber
    ) {
        try {
            return telecomService.recordingProcess(recordingUrl,fromNumber);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "/final-process/{department}", produces = MediaType.APPLICATION_XML_VALUE)
    public String finalProcess(
            @PathVariable("department") String encodedDepartment,
            @RequestParam("Digits") String digits
    ) {
        try{
            return telecomService.determineTargetNumber(encodedDepartment,digits);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
//    @PostMapping("/recording-status")
//    public void handleRecordingStatus(
//            @RequestParam("RecordingUrl") String recordingUrl,
//            @RequestParam("RecordingSid") String recordingSid,
//            @RequestParam("From") String fromNumber
//    ) {
//        // 1. 음성 파일 S3에 저장
//        String audioFileName = String.format(
//                "%s/%s_%s.wav",
//                new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
//                new SimpleDateFormat("HHmmss").format(new Date()),
//                fromNumber
//        );
//        s3Service.uploadAudioFromUrl(recordingUrl, audioFileName);
//
//        // 2. Google STT 변환
//        String transcribedText = googleSpeechService.transcribe(recordingUrl);
//
//        // 3. DB에 저장
//        complaintRepository.save(new Complaint(fromNumber, transcribedText, audioFileName));
//    }
//    @PostMapping(value = "/process-recording", produces = MediaType.APPLICATION_XML_VALUE)
//    public String processRecording(@RequestParam("RecordingUrl") String recordingUrl,
//                                   @RequestParam("RecordingSid") String recordingSid,
//                                   @RequestParam("From") String fromNumber) {
//        try {
//
//            String audioFileName = String.format(
//                    "%s/%s_%s.wav",
//                    new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
//                    new SimpleDateFormat("HHmmss").format(new Date()),
//                    fromNumber
//            );
//
//            Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
//            Thread.sleep(2000);  // 2초 대기
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
//            s3Service.uploadAudio(recordingUrl,audioFileName);
////            String savedUrl = s3Service.uploadText(speechResult, "transcripts/" + callSid + ".txt");
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
//    @PostMapping(value = "/confirm-recording", produces = MediaType.APPLICATION_XML_VALUE)
//    public String confirmRecording(
//            @RequestParam("From") String fromNumber,
//            @RequestParam(value = "SpeechResult", required = false) String speechResult
//    ) throws MalformedURLException {
//        if (speechResult == null) {
//            return """
//        <Response>
//            <Say language="ko-KR">죄송합니다. 음성 인식에 실패했습니다. 다시 시도해주세요.</Say>
//            <Redirect method="POST">/twilio/record?Digits=1</Redirect>
//        </Response>
//        """;
//        }
//
//        String department = sendToAIServer(speechResult);
//
//        // URL 인코딩
//        String encodedDepartment =  URLEncoder.encode(department, StandardCharsets.UTF_8);
//
//        String fileName = String.format(
//                "%s/%s_%s.txt",
//                new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
//                new SimpleDateFormat("HHmmss").format(new Date()),
//                fromNumber
//        );
//
//        s3Service.uploadText(speechResult, fileName);
//
//        log.info("민원 내용: {}, 배정 부서: {}", speechResult, department);
//
//        return """
//    <Response>
//        <Say language="ko-KR">고객님께서 말씀하신 민원 내용은 다음과 같습니다.</Say>
//        <Say language="ko-KR">%s 민원, 이에 적합한 부서는 %s 부서로 판단됩니다.</Say>
//        <Say language="ko-KR">해당 부서로 연결에 동의하시면 1번,
//        다시 말씀하시려면 2번을 눌러주세요.</Say>
//        <Gather input="dtmf"
//                action="/twilio/final-process/%s"
//                numDigits="1"
//                timeout="5"/>
//    </Response>
//    """.formatted(speechResult, department, encodedDepartment);
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
}