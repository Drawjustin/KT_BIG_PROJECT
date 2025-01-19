package com.example.demo.example;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/test")
public class Example2 {

    @PostMapping
    public String handleCall(@RequestParam Map<String, String> params) {
        // Twilio가 보낸 요청 데이터 출력
        System.out.println("Incoming call from: " + params.get("From"));
        System.out.println("To: " + params.get("To"));

        // TwiML 응답 반환
        return "<Response><Say>hahahahahahaha</Say></Response>";
    }
}

