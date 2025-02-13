package com.example.demo.controller;


import com.example.demo.service.TelecomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/telecoms")
@RequiredArgsConstructor
public class TelecomControllerV2 {

    private final TelecomService telecomService;

    @GetMapping("/")
    public ResponseEntity<?> healthy() {
        return ResponseEntity.ok().build();
    }

//    @PostMapping(value = "/incoming", produces = MediaType.APPLICATION_XML_VALUE)
//    public String handleIncomingCall() {
//        return """
//        <Response>
//            <Say language="ko-KR" >안녕하세요 </Say>
//            <Gather action="/telecoms/record" method="POST" numDigits="1"/>
//        </Response>
//        """;
//    }
    @PostMapping(value = "/incoming", produces = MediaType.APPLICATION_XML_VALUE)
    public String startRecording() {
        return """
       <Response>
           <Say language="ko-KR">안녕하십니까. 민원상담 자동응답 시스템입니다. 
       접수하실 민원 내용을 말씀해 주시면 담당 부서로 전달하여 처리해 드리겠습니다.
       할말이 끝난뒤에는 2번을 눌러주세요.</Say>
           <Record 
               action="/telecoms/process-recording"
               maxLength="300"
               trim="trim-silence"
               actionTimeout="60"/>
       </Response>
       """;
    }
//    @PostMapping(value = "/incoming", produces = MediaType.APPLICATION_XML_VALUE)
//    public String startRecording() {
//        return """
//       <Response>
//           <Say language="ko-KR">안녕하십니까. 민원상담 자동응답 시스템입니다.
//       접수하실 민원 내용을 말씀해 주시면 담당 부서로 전달하여 처리해 드리겠습니다.
//       할말이 끝난뒤에는 2번을 눌러주세요.</Say>
//           <Record
//               action="/telecoms/process-recording"
//               maxLength="300"
//               trim="trim-silence"
//               actionTimeout="60"/>
//       </Response>
//       """;
//    }
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

    @PostMapping(value = "/final-process/{teamInfo}", produces = MediaType.APPLICATION_XML_VALUE)
    public String finalProcess(
            @PathVariable("teamInfo") String encodedTeamInfo,
            @RequestParam("Digits") String digits
    ) {
        try{
            return telecomService.determineTargetNumber(encodedTeamInfo,digits);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}